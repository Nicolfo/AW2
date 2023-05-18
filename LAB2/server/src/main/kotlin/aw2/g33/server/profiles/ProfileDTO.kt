package aw2.g33.server.profiles

data class ProfileDTO (
    val email:String,
    val name:String,
    //val role:String
)
fun Profile.toDTO():ProfileDTO{
    return ProfileDTO(this.email,this.name/*,this.role*/)
}

fun ProfileDTO.toProfile():Profile{
    return Profile(this.email,this.name)
}