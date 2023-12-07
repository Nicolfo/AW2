package aw2.g33.server.messages

import aw2.g33.server.attachment.Attachment
import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.profiles.ProfileRepository
import aw2.g33.server.profiles.toProfile
import aw2.g33.server.tickets.*
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class MessageServiceImpl(
    private val messageRepository: MessageRepository,
    private val profileRepository: ProfileRepository,
    val ticketRepository: TicketRepository
) : MessageService {
    override fun sendMessage(ticketID: UUID, messageDTO: MessageDTO): Message? {
        return try {
            var ticket = ticketRepository.getReferenceById(ticketID);
            var message = Message(
                ticket,
                messageDTO.content!!,
                profileRepository.getReferenceById(messageDTO.sender!!),
                messageDTO.type.toString()
            )
            messageRepository.save(message)
        } catch (e: Exception) {
            //throw TicketIDNotFoundException("Cannot send a message to a chat with this ticketID");
            null;
        }
    }


    override fun receiveAllMessagesByTicket(ticketID: UUID): List<MessageDTO> {

        try {
            var listOfMessages = messageRepository.findByTicketID(ticketID)
            return listOfMessages.map { it.toDTO() }
        } catch (e: Exception) {
            println("error")
            //throw TicketIDNotFoundException("Cannot find a chat with this ticketID");
            return listOf()
        }
        /*if(listOfMessages.isEmpty()){
            throw EmptyChatException("Cannot retrieve a chat for the specified ticket")
        }*/
    }
}
