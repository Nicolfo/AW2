package aw2.g33.server.security


import aw2.g33.server.profiles.ProfileService
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
class SecurityController (private val userService: UserService){
    @Value("\${KEYCLOAK_IP}")
    lateinit var ip :String

    @PostMapping("/user/validate/")
    @ResponseStatus(HttpStatus.OK)
    fun userValidate(@RequestBody userDTO: UserDTO):String{

        println(ip);
        println(userDTO.toString())
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

    @PostMapping("/user/signup/")
    @ResponseStatus(HttpStatus.OK)
    fun userSignup(@RequestBody userDTO: UserDTO): ResponseEntity<URI> {

        val response = userService.create(userDTO)

        if (response.status != 201)
            return ResponseEntity.internalServerError().build()
        else {
            val role = userService.findRoleByName("ROLE_Client")
            userService.assignRole(userDTO.username, role)
            return ResponseEntity.created(response.location).build()
        }


        //aggiungere anche altri userdetails (email) e salvare user nel nostro database dei profile??

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