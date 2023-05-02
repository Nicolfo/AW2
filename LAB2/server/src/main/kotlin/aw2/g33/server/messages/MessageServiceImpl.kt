package aw2.g33.server.messages

import aw2.g33.server.attachment.Attachment
import aw2.g33.server.tickets.Ticket

class MessageServiceImpl:MessageService {
    override fun sendMessage(text: String, attachments: List<Attachment>, ticket: Ticket) {
        TODO("Not yet implemented")
    }

    override fun reciveAllMessages(ticket: Ticket) {
        TODO("Not yet implemented")
    }
}