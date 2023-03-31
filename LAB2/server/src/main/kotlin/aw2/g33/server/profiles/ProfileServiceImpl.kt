package aw2.g33.server.profiles


import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProfileServiceImpl(private val profileRepository: ProfileRepository):ProfileService {
    override fun addProfile(profile: ProfileDTO): ProfileDTO? {
        if(profileRepository.findByIdOrNull(profile.email)==null) {
            profileRepository.save(profile.toProfile())
            return profile
        }
        else
            throw EmailConflictException("Email already used")
    }

    override fun getProfileInfo(email: String): ProfileDTO? {
        return profileRepository.findByIdOrNull(email)?.toDTO() ?: throw PrimaryKeyNotFoundException("Email not found in DB")
    }

    override fun updateProfile(email: String, profile: ProfileDTO): ProfileDTO? {

        if(profileRepository.findByIdOrNull(email)==null)           //se non esisteva vecchia mail
            throw PrimaryKeyNotFoundException("Email not found in DB")
        if(profileRepository.findByIdOrNull(profile.email)!=null && email!=profile.email)   //se email nuova esiste già
            throw EmailConflictException("New Email is already used")

        profileRepository.deleteById(email)
        profileRepository.save(Profile(profile.email,profile.name))
        return profile
    }

}