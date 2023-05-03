package aw2.g33.server.messages

import aw2.g33.server.attachment.Attachment
import aw2.g33.server.profiles.Profile
import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.profiles.toProfile
import aw2.g33.server.tickets.Ticket
import aw2.g33.server.tickets.TicketDTO
import aw2.g33.server.tickets.toTicket
import org.springframework.stereotype.Service

@Service
class MessageServiceImpl(private val messageRepository: MessageRepository):MessageService {
     override fun sendMessage(text: String, attachments: List<Attachment>, ticket: TicketDTO, writer: ProfileDTO) {
        var message = Message(ticket.toTicket(),text,writer.toProfile(),attachments)
        messageRepository.save(message);
        //TODO("Check input")
    }

    override fun reciveAllMessagesByTicket(ticket: TicketDTO) :List<Message>{
        return messageRepository.findByTicket(ticket.toTicket())
        //TODO("Not yet implemented")
    }
}