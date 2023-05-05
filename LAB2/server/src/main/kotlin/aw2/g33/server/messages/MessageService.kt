package aw2.g33.server.messages

import aw2.g33.server.attachment.Attachment
import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.tickets.TicketDTO


interface MessageService {
    fun sendMessage(text:String,ticketDTO:TicketDTO,writer: ProfileDTO,numberOfAttachment:Int):Long
    fun receiveAllMessagesByTicket(ticketDTO: TicketDTO):List<Message>
    fun addAttachmentToMessage(message:Message,attachment: Attachment)
}