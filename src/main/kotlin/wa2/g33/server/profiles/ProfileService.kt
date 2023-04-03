package wa2.g33.server.profiles


interface ProfileService {
    fun getProfile(email: String): ProfileDTO?

    fun addProfile(profile: ProfileDTO): ProfileDTO

    fun updateProfile(email: String, profile: ProfileDTO): ProfileDTO
}