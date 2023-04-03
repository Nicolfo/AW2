package wa2.g33.server.profiles

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name="profiles")
class Profile(
    @Id
    var email: String,
    var name: String
){}

fun ProfileDTO.toDAO(): Profile{
    return Profile(email,name)
}
