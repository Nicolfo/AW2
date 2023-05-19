package aw2.g33.server.profiles

interface ProfileService {
    fun addProfile(profile:ProfileDTO):ProfileDTO
    fun getProfileInfo(username:String):ProfileDTO
    fun updateProfile(username: String,profile: ProfileDTO):ProfileDTO
}