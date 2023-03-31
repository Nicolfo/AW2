package aw2.g33.server.profiles

data class ProfileDTO (
    val email:String,
    val name:String
)
public fun Profile.toDTO():ProfileDTO{
    return ProfileDTO(this.email,this.name)
}

public fun ProfileDTO.toProfile():Profile{
    return Profile(this.email,this.name)
}