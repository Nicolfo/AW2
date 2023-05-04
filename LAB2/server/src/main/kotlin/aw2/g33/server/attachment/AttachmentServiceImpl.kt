package aw2.g33.server.attachment

import aw2.g33.server.messages.MessageRepository
import aw2.g33.server.messages.MessageService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class AttachmentServiceImpl (private val messageRepository: MessageRepository, private val messageService: MessageService):AttachmentService{
    override fun addAttachmentToMessage(messageId: Long, file: MultipartFile) {
        var message=messageRepository.findById(messageId);
        if(message.isEmpty){
            throw MessageNotFoundException("message cannot be found on db")
        }

        var attachmentToAdd:Attachment=Attachment(message.get(),file.bytes,file.contentType!!,file.originalFilename!!);
        messageService.addAttachmentToMessage(message.get(),attachmentToAdd);
    }
}