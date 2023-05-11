package aw2.g33.server.security


import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers


@RestController
@CrossOrigin
class SecurityController (){
    @PostMapping("/user/validate/")
    @ResponseStatus(HttpStatus.OK)
    fun userValidate(@RequestBody userDTO: UserDTO):String{

        println(userDTO.toString())
        val url = "http://localhost:8080/realms/Client/protocol/openid-connect/token"


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
            //throw Exception("Cannot log in, username or password incorrect")
        }
        return response.body()
        //val userDetails = SecurityContextHolder.getContext().authentication.principal
    }
}