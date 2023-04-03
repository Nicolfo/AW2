package wa2.g33.server.profiles

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class ProfileController(
    private val profileService: ProfileService
)  {
    @GetMapping("/API/profiles/{email}")
    @ResponseStatus(HttpStatus.OK)
    fun getProfile(@PathVariable email: String): ProfileDTO?{
        return profileService.getProfile(email)
    }

    @PostMapping("/API/profiles")
    @ResponseStatus(HttpStatus.CREATED)
    fun addProfile(@RequestBody profile: ProfileDTO?): ProfileDTO{
        if(profile==null){
            throw RequestBodyException("Request Body format is incorrect")
        }
        return profileService.addProfile(profile)
    }

    @PutMapping("/API/profiles/{email}")
    @ResponseStatus(HttpStatus.OK)
    fun updateProfile(@PathVariable email: String, @RequestBody profile: ProfileDTO?): ProfileDTO?{
        if(profile==null){
            throw RequestBodyException("Request Body format is incorrect")
        }
        return profileService.updateProfile(email,profile)
    }

}