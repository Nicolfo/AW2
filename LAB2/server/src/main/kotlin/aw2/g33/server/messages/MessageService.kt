package aw2.g33.server.messages

import aw2.g33.server.attachment.Attachment
import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.tickets.TicketDTO
import java.util.UUID


interface MessageService {
    fun sendMessage(ticketDTO: UUID,messageDTO: MessageDTO):Message
    fun receiveAllMessagesByTicket(ticketDTO: UUID):List<MessageDTO>

}
