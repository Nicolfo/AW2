package aw2.g33.server.attachment

import aw2.g33.server.profiles.Profile
import aw2.g33.server.profiles.ProfileDTO
import java.util.*

data class AttachmentDTO (var id: UUID, var fileName: String?)
    fun Attachment.toDTO(): AttachmentDTO {
        return AttachmentDTO(this.attachmentId!!,this.fileName)
    }
