package aw2.g33.server.messages

import aw2.g33.server.attachment.Attachment
import aw2.g33.server.attachment.AttachmentService
import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.profiles.ProfileRepository
import aw2.g33.server.profiles.toProfile
import aw2.g33.server.tickets.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MessageServiceImpl(
    private val messageRepository: MessageRepository,
    private val profileRepository: ProfileRepository,
    private val attachmentService: AttachmentService,
    val ticketRepository: TicketRepository
) : MessageService {
    override fun sendMessage(ticketID: UUID, messageDTO: MessageDTO,ignoreStatus:Boolean): Message? {
        return try {
            var ticket = ticketRepository.getReferenceById(ticketID);
            if(!ignoreStatus && ticket.status.compareTo("IN PROGRESS") != 0)
                return null

            var message: Message = if (messageDTO.files.isEmpty())
                Message(
                    ticket,
                    messageDTO.content!!,
                    profileRepository.getReferenceById(messageDTO.sender!!),
                    messageDTO.type.toString(),
                )
            else
                Message(
                    ticket,
                    messageDTO.content!!,
                    profileRepository.getReferenceById(messageDTO.sender!!),
                    messageDTO.type.toString(),
                    messageDTO.files.map { attachmentService.getFileByID(it.id) }.toMutableList()
                )
            messageRepository.save(message)
        } catch (e: Exception) {
            //throw TicketIDNotFoundException("Cannot send a message to a chat with this ticketID");
            null;
        }
    }

    @Transactional(readOnly = true)
    override fun receiveAllMessagesByTicket(ticketID: UUID): List<MessageDTO> {
        return try {
            var listOfMessages = messageRepository.findByTicketID(ticketID)
            listOfMessages.map { it.toDTO() }
        } catch (e: Exception) {
            println("error " + e.message)
            //throw TicketIDNotFoundException("Cannot find a chat with this ticketID");
            listOf()
        }
        /*if(listOfMessages.isEmpty()){
            throw EmptyChatException("Cannot retrieve a chat for the specified ticket")
        }*/
    }


}
