package aw2.g33.server.profiles

interface ProfileService {
    fun addProfile(profile:ProfileDTO):ProfileDTO?;
    fun getProfileInfo(email:String):ProfileDTO?;
    fun updateProfile(email: String,profile: ProfileDTO):ProfileDTO?;
}