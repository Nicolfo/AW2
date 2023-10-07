package aw2.g33.server.messages


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MessageRepository :JpaRepository<Message,UUID>{
    @Query("select * from message where ticket_id = :ticketId ", nativeQuery = true)
    fun findByTicketID(ticketId: UUID?):List<Message>
}
