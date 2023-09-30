package aw2.g33.server.attachment

import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@CrossOrigin
class AttachmentController(private val attachmentService: AttachmentService) {


    @PutMapping("/uploadFile")
    @ResponseStatus(HttpStatus.OK)
    fun addAttachment(@RequestPart("file") file: MultipartFile): AttachmentDTO {
        //var a = Attachment(file.bytes, file.contentType!!, file.originalFilename!!);
        attachmentService.addAttachment(file)

        return attachmentService.addAttachment(file)
    }

    @GetMapping("/getFile/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getFile(@PathVariable id: UUID): ResponseEntity<ByteArrayResource> {
        var elem = attachmentService.getFileByID(id)
        if (elem?.attachment != null) {
            val resource = ByteArrayResource(elem.attachment!!)

            val headers = HttpHeaders()
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${elem.fileName}")
            headers.contentType = MediaType.APPLICATION_OCTET_STREAM

            return ResponseEntity.ok()
                .headers(headers)
                .contentLength(elem.attachment!!.size.toLong())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource)
        } else {
            return ResponseEntity.notFound().build()
        }
    }
}