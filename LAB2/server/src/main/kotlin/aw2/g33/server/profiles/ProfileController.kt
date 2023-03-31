package aw2.g33.server.profiles

import aw2.g33.server.products.ProductDTO
import org.apache.tomcat.util.json.JSONParser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProfileController(private val profileService: ProfileService) {
    @GetMapping("/API/profiles/{email}")
    fun getProfile(@PathVariable email:String):ProfileDTO?{
        return profileService.getProfileInfo(email)
    }

    @PostMapping("/API/profiles")
    fun insertProfile(@RequestBody body:String){
        val parsed= JSONParser(body).parseObject();
        if(!parsed.containsKey("name") || !parsed.containsKey("email")){
            return error("Error Body")
        }
         profileService.addProfile(ProfileDTO(parsed["email"].toString(),parsed.get("name").toString()))

    }
    @PutMapping("/API/profiles/{email}")
    fun updateProfile(@PathVariable email:String,@RequestBody body:String){
        val parsed= JSONParser(body).parseObject();
        if(!parsed.containsKey("name") || !parsed.containsKey("email")){
            return error("Error Body")
        }
        profileService.updateProfile(email,ProfileDTO(parsed["email"].toString(),parsed.get("name").toString()))
    }
}