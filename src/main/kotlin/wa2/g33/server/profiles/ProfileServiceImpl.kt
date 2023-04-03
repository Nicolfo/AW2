package wa2.g33.server.profiles

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import wa2.g33.server.products.ProductNotFoundException

@Service
class ProfileServiceImpl(
    private val profileRepository: ProfileRepository
): ProfileService {
    override fun getProfile(email: String): ProfileDTO? {
        return profileRepository.findByIdOrNull(email)
            ?.toDTO()
            ?: throw ProductNotFoundException(
                "profile with email: $email not found"
            )
    }

    override fun addProfile(profile: ProfileDTO): ProfileDTO {
        if (!profileRepository.existsById(profile.email)) {
            return profileRepository.save(profile.toDAO()).toDTO()
        } else throw DuplicateProfileException(
            "profile with email: $profile.email already exists"
        )
    }

    override fun updateProfile(email: String, profile: ProfileDTO): ProfileDTO {
        if (!profileRepository.existsById(email))
            throw ProfileNotFoundException("Profile with email: $email not found")
        if (profileRepository.existsById(profile.email) && email != profile.email)
            throw DuplicateProfileException("profile with email: $profile.email already exists")

        profileRepository.deleteById(email)
        return profileRepository.save(profile.toDAO()).toDTO()
    }

}