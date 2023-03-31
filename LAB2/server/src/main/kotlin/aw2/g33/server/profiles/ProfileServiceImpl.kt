package aw2.g33.server.profiles

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProfileServiceImpl(private val profileRepository: ProfileRepository):ProfileService {
    override fun addProfile(profile: ProfileDTO): ProfileDTO? {
        if(profileRepository.findByIdOrNull(profile.email)==null) {
            profileRepository.save(profile.toProfile())
            return profile;
        }
        else
            return null;
    }

    override fun getProfileInfo(email: String): ProfileDTO? {
        return profileRepository.findByIdOrNull(email)?.toDTO();
    }

    override fun updateProfile(email: String, profile: ProfileDTO): ProfileDTO? {

        if(profileRepository.findByIdOrNull(email)==null)           //se non esisteva vecchia mail
            return null
        if(profileRepository.findByIdOrNull(profile.email)!=null)   //se email nuova non valida
            return null

        profileRepository.deleteById(email);
        profileRepository.save(Profile(profile.email,profile.name));
        return profile;
    }

}