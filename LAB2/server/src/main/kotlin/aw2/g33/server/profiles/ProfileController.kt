package aw2.g33.server.profiles

import aw2.g33.server.products.ProductDTO
import org.apache.tomcat.util.json.JSONParser
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
class ProfileController(private val profileService: ProfileService) {
    @GetMapping("/API/profiles/{email}")
    fun getProfile(@PathVariable email:String):ProfileDTO?{
        return profileService.getProfileInfo(email)
    }

    @PostMapping("/API/profiles/")
    fun insertProfile(@RequestBody body:String): ProfileDTO {
        val parsed= JSONParser(body).parseObject();
        if(!parsed.containsKey("name") || !parsed.containsKey("email")){
            throw RequestBodyException("Request Body format is incorrect")
        }
        val profile_to_add=ProfileDTO(parsed["email"].toString(),parsed.get("name").toString())

        profileService.addProfile(profile_to_add)
        return profile_to_add
    }
    @PutMapping("/API/profiles/{email}")
    fun updateProfile(@PathVariable email:String,@RequestBody body:String): ProfileDTO {
        val parsed= JSONParser(body).parseObject();
        if(!parsed.containsKey("name") || !parsed.containsKey("email")){
            throw RequestBodyException("Request Body format is incorrect")
        }
        val profile_to_update=ProfileDTO(parsed["email"].toString(),parsed.get("name").toString())
        profileService.updateProfile(email,profile_to_update)
        return profile_to_update
    }
}
