package aw2.g33.server.security


import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.profiles.ProfileService
import io.micrometer.observation.annotation.Observed
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
@Observed
class SecurityController (private val userService: UserService,private val profileService: ProfileService){
    @Value("\${KEYCLOAK_IP}")
    lateinit var ip :String

    @PostMapping("/user/validate/")
    @ResponseStatus(HttpStatus.OK)
    fun userValidate(@RequestBody userDTO: UserDTO):String{
        val url = "http://${ip}:8080/realms/AW2-Auth-Realm/protocol/openid-connect/token"


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
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Observed
    fun userSignup(@RequestBody userDTO: UserDTO): ResponseEntity<URI> {

        val response = userService.create(userDTO)

        if (response.status != 201) {
            if (response.status == 409) {
                throw UsernameAlreadyExistException("Username already exists, cannot create")
                // oppure direttamente al client:
                // return ResponseEntity.status(HttpStatus.CONFLICT).build()
            }
            else throw RuntimeException("User was not created") //o altre eccezioni specifiche
        }
        else {
            val role = userService.findRoleByName("Client")
            userService.assignRoleWithUsername(userDTO.username, role)
            val profile = profileService.addProfile(ProfileDTO(userDTO.email,userDTO.username,role.name))
            return ResponseEntity.created(response.location).build()
        }

    }

    @PostMapping("/user/createExpert")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_Manager')")  // serve? o basta la restrizione in SecurityConfiguration?
    @Transactional
    fun createExpert(@RequestBody userDTO: UserDTO): ResponseEntity<URI> {

        val response = userService.create(userDTO)

        if (response.status != 201) {
            if (response.status == 409) {
                throw UsernameAlreadyExistException("Username already exists, cannot create")
                // oppure direttamente al client:
                // return ResponseEntity.status(HttpStatus.CONFLICT).build()
            }
            else throw RuntimeException("User was not created") //o altre eccezioni specifiche
        }
        else {
            val role = userService.findRoleByName("Expert")
            userService.assignRoleWithUsername(userDTO.username, role)
            profileService.addProfile(ProfileDTO(userDTO.email,userDTO.username,role.name))
            return ResponseEntity.created(response.location).build()
        }

    }


        /*@GetMapping("/user/get_info")
        @ResponseStatus(HttpStatus.OK)
        fun userInfoByAccessToken( ):Any{

            val roles = SecurityContextHolder.getContext().authentication.authorities.filter { it.authority.contains("ROLE") }
            val username= SecurityContextHolder.getContext().authentication.name
            return username

            /*val resourceAccess: Map<String, Any> = userDetails.getClaim("realm_access")
            var resourceRoles: Collection<String> = resourceAccess["roles"] as Collection<String>;
            return resourceRoles.first()*/

           // return jwtAuthConverter.convert(userDetails).authorities.contains(SimpleGrantedAuthority("ROLE_Client")

            //return userDetails
            //jwtAuthConverter.extractResourceRoles(userDetails)

            //val ret=jwtAuthConverter.convert( userDetails)


           // return jwtAuthConverter.extractResourceRoles(userDetails).toString()
        }*/
}