package aw2.g33.server.attachment

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@CrossOrigin
class AttachmentController (private val attachmentService: AttachmentService){
    @PutMapping("/API/attachment/add/{messageId}")
    @ResponseStatus(HttpStatus.OK)
    fun addAttachment(@PathVariable messageId:Long,@RequestPart("file") file :MultipartFile){
        attachmentService.addAttachmentToMessage(messageId,file);
    }
}