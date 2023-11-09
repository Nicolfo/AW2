package aw2.g33.server.security

data class UserDTO2 (
    val oldUserName: String,
    val newUserName:String,
    val newEmail:String,
    val newRole:String
){
}