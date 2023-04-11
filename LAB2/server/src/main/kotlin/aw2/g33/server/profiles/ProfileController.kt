package aw2.g33.server.profiles


import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
class ProfileController(private val profileService: ProfileService) {
    @GetMapping("/API/profiles/{email}")
    @ResponseStatus(HttpStatus.OK)
    fun getProfile(@PathVariable email:String):ProfileDTO?{
        return profileService.getProfileInfo(email)
    }

    @GetMapping("/API/profiles/")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun getProfileWithNoParam(){
        throw RequestParamException("GET request at /API/profiles/ must include an email as param")
    }


    @PostMapping("/API/profiles/")
    @ResponseStatus(HttpStatus.CREATED)
    fun insertProfile(@RequestBody profileToAdd:ProfileDTO): ProfileDTO {
        if(profileToAdd.email=="" || profileToAdd.name==""){
            throw RequestBodyException("Name and mail field cannot be empty!")
        }
        return profileService.addProfile(profileToAdd)
    }

    @PutMapping("/API/profiles/{email}")
    @ResponseStatus(HttpStatus.OK)
    fun updateProfile(@PathVariable email:String,@RequestBody profileToUpdate:ProfileDTO): ProfileDTO {

        if(profileToUpdate.email=="" || profileToUpdate.name==""){
            throw RequestBodyException("Name and email field cannot be empty!")
        }
        return profileService.updateProfile(email,profileToUpdate)
    }
    @PutMapping("/API/profiles/")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun updateProfileWithNoParam(){
        throw RequestParamException("PUT request at /API/profiles/ must include an email as param")
    }

}
