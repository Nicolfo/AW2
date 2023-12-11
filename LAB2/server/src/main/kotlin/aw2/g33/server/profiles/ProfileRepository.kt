package aw2.g33.server.profiles


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ProfileRepository : JpaRepository<Profile, String>{
    fun findByUsernameOrEmail(username: String, email: String):Optional<Profile>
}