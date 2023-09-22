package aw2.g33.server.security

import aw2.g33.server.profiles.ProfileDTO

data class UserDTO (
    val email:String?,
    val username:String,
    val password:String
){
    override fun toString():String{
        val email:String;
        if(this.email!=null)
            email="Email: "+this.email
        else
            email=""


        return ("Username: "+this.username+" Password: "+this.password + email)
    }
}