package aw2.g33.server.profiles

import aw2.g33.server.products.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProfileServiceImpl(private val profileRepository: ProfileRepository):ProfileService {
    override fun addProfile(profile: ProfileDTO): ProfileDTO? {
        TODO("Not yet implemented")
    }

    override fun getProfileInfo(email: String): ProfileDTO {
        TODO("Not yet implemented")
    }

    override fun updateProfile(email: String, profile: ProfileDTO): ProfileDTO? {
        TODO("Not yet implemented")
    }

}