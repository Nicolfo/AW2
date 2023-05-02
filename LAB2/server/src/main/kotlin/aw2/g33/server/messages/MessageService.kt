package aw2.g33.server.messages

import aw2.g33.server.attachment.Attachment
import aw2.g33.server.tickets.Ticket

interface MessageService {
    fun sendMessage(text:String,attachments:List<Attachment>,ticket:Ticket);
    fun reciveAllMessages(ticket: Ticket);
}