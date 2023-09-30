package aw2.g33.server.messages

import aw2.g33.server.attachment.Attachment
import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.profiles.ProfileRepository
import aw2.g33.server.profiles.toProfile
import aw2.g33.server.tickets.TicketDTO
import aw2.g33.server.tickets.TicketRepository
import aw2.g33.server.tickets.TicketService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class MessageServiceImpl(private val messageRepository: MessageRepository,private val profileRepository: ProfileRepository,val ticketRepository: TicketRepository):MessageService {
    override fun sendMessage(ticketID: UUID, messageDTO: MessageDTO): Message {
        var message=Message(ticketRepository.getReferenceById(ticketID),
            messageDTO.content!!,profileRepository.getReferenceById(messageDTO.sender!!),messageDTO.type.toString())
       return messageRepository.save(message)
    }







    override fun receiveAllMessagesByTicket(ticketID: UUID) :List<MessageDTO>{
        var listOfMessages= messageRepository.findByTicketID(ticketID)
        if(listOfMessages.isEmpty()){
            throw EmptyChatException("Cannot retrieve a chat for the specified ticket")
        }
        return listOfMessages.map{it.toDTO()}
    }
}
