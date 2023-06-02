package aw2.g33.server.security


import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.profiles.ProfileService
import jakarta.transaction.Transactional
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


@RestController
@CrossOrigin
class SecurityController (private val userService: UserService,private val profileService: ProfileService){
    @Value("\${KEYCLOAK_IP}")
    lateinit var ip :String

    @PostMapping("/user/validate/")
    @ResponseStatus(HttpStatus.OK)
    fun userValidate(@RequestBody userDTO: UserDTO):String{
        val url = "http://${ip}:8080/realms/AW2-Auth-Realm/protocol/openid-connect/token"
        println("url "+url+" username "+userDTO.username+" password "+userDTO.password)

        //con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")


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
        return response.body()

    }

    @PostMapping("/user/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    fun userSignup(@RequestBody userDTO: UserDTO){
        val role = userService.findRoleByName("Client")
        val profile = profileService.addProfile(ProfileDTO(userDTO.email,userDTO.username,role.name))

        val response = userService.create(userDTO)

        if (response.status != 201) {
            profileService.removeProfile(userDTO.username)
            if (response.status == 409) {
                throw UsernameAlreadyExistException("Username already exists, cannot create")
            }
            else throw RuntimeException("User was not created") //o altre eccezioni specifiche
        }
        else {
            userService.assignRoleWithUsername(userDTO.username, role)
        }

    }

    @PostMapping("/user/createExpert")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_Manager')") 
    @Transactional
    fun createExpert(@RequestBody userDTO: UserDTO): ResponseEntity<URI> {
        val role = userService.findRoleByName("Expert")
        profileService.addProfile(ProfileDTO(userDTO.email,userDTO.username,role.name))

        val response = userService.create(userDTO)

        if (response.status != 201) {
            profileService.removeProfile(userDTO.username)
            if (response.status == 409) {

                throw UsernameAlreadyExistException("Username already exists, cannot create")
            }
            else throw RuntimeException("User was not created") //o altre eccezioni specifiche
        }
        else {

            userService.assignRoleWithUsername(userDTO.username, role)

            return ResponseEntity.created(response.location).build()
        }

    }



}