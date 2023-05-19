package aw2.g33.server.profiles


import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProfileServiceImpl(private val profileRepository: ProfileRepository):ProfileService {
    override fun addProfile(profile: ProfileDTO): ProfileDTO {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"

        if(profile.email!==null && !emailRegex.toRegex().matches(profile.email))
            throw EmailInvalidException("Invalid Email Format")

        if(profileRepository.findByIdOrNull(profile.username)==null) {
            profileRepository.save(profile.toProfile())
            return profile
        }
        else
            throw EmailConflictException("Username already used")
    }

    override fun getProfileInfo(username: String): ProfileDTO {
        return profileRepository.findByIdOrNull(username)?.toDTO() ?: throw PrimaryKeyNotFoundException("username not found in DB")
    }

    override fun updateProfile(username: String, profile: ProfileDTO): ProfileDTO {

        if(profileRepository.findByIdOrNull(username)==null)           //se non esisteva vecchia mail
            throw PrimaryKeyNotFoundException("Email not found in DB")
        if(profileRepository.findByIdOrNull(profile.username)!=null && username!=profile.username)   //se email nuova esiste già
            throw EmailConflictException("New Username is already used")
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        if(profile.email!==null && !emailRegex.toRegex().matches(profile.email))
            throw EmailInvalidException("Invalid Email Format")

        profileRepository.deleteById(username)
        val profileToADD=profile.toProfile()
        profileRepository.save(profileToADD)
        return profile;
    }

}