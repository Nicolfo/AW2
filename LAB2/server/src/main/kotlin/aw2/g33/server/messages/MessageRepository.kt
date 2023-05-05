package aw2.g33.server.messages


import aw2.g33.server.tickets.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository :JpaRepository<Message,Long>{
    fun findByTicket(ticket: Ticket):List<Message>
}