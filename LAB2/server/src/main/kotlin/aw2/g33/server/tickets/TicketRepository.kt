package aw2.g33.server.tickets

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TicketRepository : JpaRepository<Ticket, UUID>{
    fun findTicketsByStatus(status:String):List<Ticket>

    @Query("select * from ticket where customer_username = :username or worker_username =:username", nativeQuery = true)
    fun findTicketsByUsername(username:String):List<Ticket>
}
