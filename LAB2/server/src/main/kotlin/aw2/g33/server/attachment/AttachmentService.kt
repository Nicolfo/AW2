package aw2.g33.server.attachment

import org.springframework.web.multipart.MultipartFile

interface AttachmentService {
    fun addAttachmentToMessage(messageId:Long,file:MultipartFile,seqNr:Int)
}