package aw2.g33.server.profiles

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class ProfileController(private val profileService: ProfileService) {
    @GetMapping("/API/profiles/{email}")
    @ResponseStatus(HttpStatus.OK)
    fun getProfile(@PathVariable email:String):ProfileDTO?{
        return profileService.getProfileInfo(email)
    }

    @PostMapping("/API/profiles/")
    @ResponseStatus(HttpStatus.CREATED)
    fun insertProfile(@RequestBody profileToAdd:ProfileDTO?): ProfileDTO {
        if(profileToAdd==null){
            throw RequestBodyException("Request Body format is incorrect")
        }
        profileService.addProfile(profileToAdd)
        return profileToAdd
    }

    @PutMapping("/API/profiles/{email}")
    @ResponseStatus(HttpStatus.OK)
    fun updateProfile(@PathVariable email:String,@RequestBody profileToUpdate:ProfileDTO?): ProfileDTO {

        if(profileToUpdate==null){
            throw RequestBodyException("Request Body format is incorrect")
        }

        profileService.updateProfile(email,profileToUpdate)
        return profileToUpdate
    }
}