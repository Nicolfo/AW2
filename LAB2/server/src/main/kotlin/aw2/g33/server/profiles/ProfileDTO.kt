package aw2.g33.server.profiles

data class ProfileDTO (
    val email:String?,
    val username:String,
    val role:String
)
fun Profile.toDTO():ProfileDTO{
    return ProfileDTO(this.email,this.username,this.role)
}

fun ProfileDTO.toProfile():Profile{
    return Profile(this.email,this.username,this.role)
}