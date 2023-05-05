package aw2.g33.server.messages

import aw2.g33.server.attachment.Attachment
import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.profiles.toProfile
import aw2.g33.server.tickets.TicketDTO
import aw2.g33.server.tickets.TicketService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class MessageServiceImpl(private val messageRepository: MessageRepository,private val ticketService: TicketService):MessageService {
     override fun sendMessage(text: String, ticketDTO: TicketDTO, writer: ProfileDTO, numberOfAttachment:Int) :Long{
         val ticket=ticketService.ticketDTOToTicket(ticketDTO)
         if (ticket.status=="OPEN" || ticket.status=="CLOSED")
             throw TicketStatusError("Error chat is not open, in order to chat the status can't be OPEN or CLOSED")
        val message = Message(ticket,text,writer.toProfile(),numberOfAttachment)
        messageRepository.save(message)
         return message.messageId!!
    }
    @Transactional
    override fun sendMessageWithAttachments(
        text: String,
        ticketDTO: TicketDTO,
        writer: ProfileDTO,
        files: Array<ByteArray>,
        filesName: Array<String>,
        filesType: Array<String>
    ): Long {
        val ticket=ticketService.ticketDTOToTicket(ticketDTO)
        if (ticket.status=="OPEN" || ticket.status=="CLOSED")
            throw TicketStatusError("Error chat is not open, in order to chat the status can't be OPEN or CLOSED")
        val message = Message(ticket,text,writer.toProfile(),files.size)
        for(i in 0..files.size){
            message.attachments.add(Attachment(message,files[i],filesType[i],filesName[i],i))
        }
        messageRepository.save(message)
        return message.messageId!!
    }

    override fun addAttachmentToMessage(message:Message,attachment: Attachment){
        message.attachments.add(attachment)
    }



    override fun receiveAllMessagesByTicket(ticketDTO: TicketDTO) :List<Message>{
        val listOfMessages= messageRepository.findByTicket(ticketService.ticketDTOToTicket(ticketDTO))
        if(listOfMessages.isEmpty()){
            throw EmptyChatException("Cannot retrieve a chat for the specified ticket")
        }
        return listOfMessages
    }
}