package aw2.g33.server.profiles

import aw2.g33.server.security.UserService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
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
    @GetMapping("/API/profiles/{username}")
    @ResponseStatus(HttpStatus.OK)
    fun getProfile(@PathVariable username:String):ProfileDTO?{
        return profileService.getProfileInfo(username)
    }

    @GetMapping("/API/profiles/")
    @ResponseStatus(HttpStatus.OK)
    fun getProfileWithNoParam():List<ProfileDTO>{
        return profileService.getAllProfiles()
    }


 /*   @PostMapping("/API/profiles/")
    @ResponseStatus(HttpStatus.CREATED)
    fun insertProfile(@RequestBody profileToAdd:ProfileDTO): ProfileDTO {
        if( profileToAdd.username==""|| profileToAdd.role==""){
            throw RequestBodyException("username and role field cannot be empty!")
        }

        return profileService.addProfile(profileToAdd)
    }
*/
    /*@PutMapping("/API/profiles/{username}")
    @ResponseStatus(HttpStatus.OK)
    fun updateProfile(@PathVariable username:String,@RequestBody profileToUpdate:ProfileDTO): ProfileDTO {

        if(profileToUpdate.role=="" || profileToUpdate.username==""){
            throw RequestBodyException("role and usernmae field cannot be empty!")
        }
        return profileService.updateProfile(username,profileToUpdate)
    }*/
    /*@PutMapping("/API/profiles/")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun updateProfileWithNoParam(){
        throw RequestParamException("PUT request at /API/profiles/ must include an username as param")
    }*/

}
