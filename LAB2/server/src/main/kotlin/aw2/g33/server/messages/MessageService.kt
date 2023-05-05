package aw2.g33.server.messages

import aw2.g33.server.attachment.Attachment
import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.tickets.TicketDTO
import java.util.UUID

interface MessageService {
    fun sendMessage(text:String,ticket:TicketDTO,writer: ProfileDTO,numberOfAttachment:Int):Long;
    fun receiveAllMessagesByTicket(ticketID: UUID):List<Message>;
    fun addAttachmentToMessage(message:Message,attachment: Attachment)
}
