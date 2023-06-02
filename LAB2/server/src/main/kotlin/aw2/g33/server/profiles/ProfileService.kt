package aw2.g33.server.profiles

interface ProfileService {
    fun addProfile(profile:ProfileDTO):ProfileDTO
    fun removeProfile(username: String)
    fun getProfileInfo(username:String):ProfileDTO
    fun updateProfile(username: String,profile: ProfileDTO):ProfileDTO
}