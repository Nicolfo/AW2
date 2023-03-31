package aw2.g33.server.profiles

import aw2.g33.server.products.ProductDTO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ProfileController(private val profileService: ProfileService) {
    @GetMapping("/profiles/{email}")
    fun getProfile(@PathVariable email:String):ProfileDTO?{
        return profileService.getProfileInfo(email)
    }
}