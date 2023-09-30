package aw2.g33.server.attachment

import aw2.g33.server.messages.MessageRepository
import aw2.g33.server.messages.MessageService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service


class AttachmentServiceImpl (var attachmentRepository: AttachmentRepository):AttachmentService{
    override fun addAttachment(file: MultipartFile): AttachmentDTO {

        var toAdd=Attachment(file.bytes,file.contentType!!,file.name)
        return attachmentRepository.save(toAdd).toDTO()

    }

    override fun getFileByID(attachmentID: UUID): Attachment {
        return attachmentRepository.getReferenceById(attachmentID)

    }


}