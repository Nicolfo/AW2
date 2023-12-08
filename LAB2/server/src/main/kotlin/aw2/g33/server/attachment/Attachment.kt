package aw2.g33.server.attachment

import aw2.g33.server.messages.Message
import jakarta.persistence.*
import java.util.UUID

@Entity
class Attachment(
    @Lob
    @Basic(fetch = FetchType.LAZY)
    var attachment:ByteArray?=null,
    var contentType:String,
    var fileName:String,
) {
    @Id
    @GeneratedValue(generator = "uuid2")
    var attachmentId:UUID?=null

}