package aw2.g33.server.messages

import aw2.g33.server.attachment.Attachment
import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.profiles.toProfile
import aw2.g33.server.tickets.TicketDTO
import aw2.g33.server.tickets.TicketService
import org.springframework.stereotype.Service

@Service
class MessageServiceImpl(private val messageRepository: MessageRepository,private val ticketService: TicketService):MessageService {
     override fun sendMessage(text: String, ticketDTO: TicketDTO, writer: ProfileDTO,numberOfAttachment:Int) :Long{
         var ticket=ticketService.ticketDTOToTicket(ticketDTO);
         if (ticket.status=="OPEN" || ticket.status=="CLOSED")
             throw TicketStatusError("Error chat is not open, in order to chat the status can't be OPEN or CLOSED")
        var message = Message(ticket,text,writer.toProfile(),numberOfAttachment)
        messageRepository.save(message);
         return message.message_id!!;
    }

    override fun addAttachmentToMessage(message:Message,attachment: Attachment){
        message.attachments.add(attachment);
    }



    override fun receiveAllMessagesByTicket(ticketDTO: TicketDTO) :List<Message>{
        var listOfMessages= messageRepository.findByTicket(ticketService.ticketDTOToTicket(ticketDTO))
        if(listOfMessages.isEmpty()){
            throw EmptyChatException("Cannot retrieve a chat for the specified ticket")
        }
        return listOfMessages
    }
}