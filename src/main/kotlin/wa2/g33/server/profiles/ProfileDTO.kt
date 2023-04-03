package wa2.g33.server.profiles

data class ProfileDTO(
    val email: String,
    val name: String
)

fun Profile.toDTO(): ProfileDTO{
    return ProfileDTO(email,name)
}