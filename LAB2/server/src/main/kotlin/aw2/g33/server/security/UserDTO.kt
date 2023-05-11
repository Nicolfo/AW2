package aw2.g33.server.security

data class UserDTO(
    val username:String,
    val password:String
) {
    override fun toString():String{
        return ("Username: "+this.username+" Password: "+this.password)
    }
}