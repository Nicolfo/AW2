package aw2.g33.server.attachment

import aw2.g33.server.messages.MessageRepository
import aw2.g33.server.messages.MessageService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service


class AttachmentServiceImpl (private val messageRepository: MessageRepository, private val messageService: MessageService):AttachmentService{
    @Transactional
    override fun addAttachmentToMessage(messageId: Long, file: MultipartFile, seqNr:Int) {
        val message=messageRepository.findById(messageId)

        if(message.isEmpty){
            throw MessageNotFoundException("message cannot be found on db")
        }
        if(seqNr>message.get().numberOfAttachment)
            throw AttachmentOutOfBoundException("Attachment sequence number is out of bound")
        if(message.get().attachments.any { it.attachmentOrder == seqNr })
            return

        val attachmentToAdd=Attachment(message.get(),file.bytes,file.contentType!!,file.originalFilename!!,seqNr)
        messageService.addAttachmentToMessage(message.get(),attachmentToAdd)
    }
}