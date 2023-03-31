package aw2.g33.server.profiles

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "profiles")
class Profile {
    @Id
    val email: String=""
    val name:String=""
}