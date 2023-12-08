package aw2.g33.server.security


import aw2.g33.server.profiles.PrimaryKeyNotFoundException
import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.profiles.ProfileService
import io.micrometer.observation.annotation.Observed
import jakarta.transaction.Transactional
import org.keycloak.jose.jwk.JWK.Use
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.security.Principal
import java.util.stream.Collectors
import javax.management.relation.Role


@RestController
@CrossOrigin
//@Observed
class SecurityController (private val userService: UserService,private val profileService: ProfileService){
    @Value("\${KEYCLOAK_IP}")
    lateinit var ip :String

    @PostMapping("/user/validate/")
    @ResponseStatus(HttpStatus.OK)
    fun userValidate(@RequestBody userDTO: UserDTO):String{
        val url = "http://${ip}:8080/realms/AW2-Auth-Realm/protocol/openid-connect/token"

        val bodyMap=mapOf(
            "grant_type" to "password",
            "client_id" to "springboot-keycloak-client",
            "username" to userDTO.username,
            "password" to userDTO.password
        )
        val body =bodyMap.map { (key, value) -> "$key=${URLEncoder.encode(value, "UTF-8")}" }.joinToString("&")
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build()

        val response = client.send(request, BodyHandlers.ofString())
        if(!response.body().contains("access_token")){
            throw WrongCredentialsExceptions("Cannot log in, username or password incorrect")
        }
        try{
            profileService.getProfileInfo(userDTO.username)
        }
            catch(ex:PrimaryKeyNotFoundException){
                var profileinfo=userService.findByUsername(userDTO.username).first()
                profileService.addProfile(ProfileDTO(profileinfo.email,profileinfo.username,userService.getRoleById(profileinfo.id).find { it=="Expert" || it=="Client" || it=="Manager" }.toString()))
            }
        return response.body()

    }

    @PostMapping("/user/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    fun userSignup(@RequestBody userDTO: UserDTO): ResponseEntity<URI>{
       return userCreationByDTOAndRole(userDTO,"Client")

    }

    @PostMapping("/user/createExpert")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_Manager')")
    @Transactional
    fun createExpert(@RequestBody userDTO: UserDTO): ResponseEntity<URI> {
        return userCreationByDTOAndRole(userDTO,"Expert")
    }
    @PostMapping("/user/createUser/{roleName}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_Manager')")
    @Transactional
    fun createUser(@RequestBody userDTO: UserDTO,@PathVariable roleName: String): ResponseEntity<URI> {
    println(userDTO)
       return userCreationByDTOAndRole(userDTO,roleName)

    }

    @GetMapping("/user/getUsersByRole")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_Manager')")
    @Transactional
    fun getUsersByRole(@RequestParam(required = true, defaultValue = "Client") roleName:String):List<UserDTO>{
        return userService.getListUserByRole(roleName);
    }

    /*@PostMapping("/user/updateUser/")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_Manager')")
    @Transactional
    fun updateUser(@RequestBody userDTO2: UserDTO2):UserDTO{      //da vedere se è corretto, non mi piace return value
        val role = userService.findRoleByName(userDTO2.newRole)
        val profile=ProfileDTO(userDTO2.newEmail,userDTO2.newUserName,userDTO2.newRole)

        try {
            userService.updateUser(userDTO2.oldUserName,,)
            profileService.updateProfile(usernameOld, profile)
        }
        catch (err:Error){
             throw RuntimeException("User was not updated");
        }
        return userDTO;

    }*/

    private fun userCreationByDTOAndRole(userDTO: UserDTO,roleName:String) :ResponseEntity<URI> {
        val role = userService.findRoleByName(roleName)
        profileService.addProfile(ProfileDTO(userDTO.email, userDTO.username, role.name))

        val response = userService.create(userDTO)

        if (response.status != 201) {
            profileService.removeProfile(userDTO.username)
            if (response.status == 409) {

                throw UsernameAlreadyExistException("Username already exists, cannot create")
            } else throw RuntimeException("User was not created")
        } else {

            userService.assignRoleWithUsername(userDTO.username, role)

            return ResponseEntity.created(response.location).build()
        }
    }
}
