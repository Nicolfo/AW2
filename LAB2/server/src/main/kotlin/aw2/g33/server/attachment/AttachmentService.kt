package aw2.g33.server.attachment

import org.springframework.web.multipart.MultipartFile
import java.util.*

interface AttachmentService {
    fun addAttachment(file:MultipartFile):AttachmentDTO
    fun getFileByID(attachmentID: UUID):Attachment
}