package aw2.g33.server.messages

import aw2.g33.server.attachment.Attachment
import aw2.g33.server.profiles.Profile
import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.tickets.Ticket
import aw2.g33.server.tickets.TicketDTO

interface MessageService {
    fun sendMessage(text:String,attachments:List<Attachment>,ticket:TicketDTO,writer: ProfileDTO);
    fun reciveAllMessagesByTicket(ticket: TicketDTO):List<Message>;
}