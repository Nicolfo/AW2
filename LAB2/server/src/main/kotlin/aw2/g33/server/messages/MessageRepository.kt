package aw2.g33.server.messages


import aw2.g33.server.messages.Message
import aw2.g33.server.ticket_logs.TicketLog
import aw2.g33.server.tickets.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MessageRepository :JpaRepository<Message,Long>{
    @Query("select * from message where ticket_id = :ticket_id ", nativeQuery = true)
    fun findByTicketID(ticket_id: UUID?):List<Message>
}
