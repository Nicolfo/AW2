package aw2.g33.server.tickets

import java.util.UUID

data class TicketDTO (
    val ticketId:UUID?,
    val description:String,
    val priority: Int,
    val status:String,
    val customerEmail:String?,
    val workerEmail:String?
)


fun Ticket.toDTO(): TicketDTO {
    return TicketDTO(this.ticketId,this.description,this.priority,this.status,this.customer?.email,this.worker?.email)
}



