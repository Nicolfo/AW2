package aw2.g33.server.tickets

import aw2.g33.server.profiles.Profile
import aw2.g33.server.profiles.ProfileDTO
import jakarta.annotation.Priority
import java.util.UUID

data class TicketDTO (
    val ticket_id:UUID?,
    val description:String,
    val priority: Int,
    val status:String,
    val customer_email:String?
)

fun Ticket.toDTO(): TicketDTO {
    return TicketDTO(this.ticket_id,this.description,this.priority,this.status,this.customer?.email)
}

fun TicketDTO.toTicket(): Ticket {
    //return Profile(this.email,this.name)
    TODO()
}