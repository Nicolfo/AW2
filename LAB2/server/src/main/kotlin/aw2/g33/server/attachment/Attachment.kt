package aw2.g33.server.attachment

import aw2.g33.server.messages.Message
import jakarta.persistence.*
import java.util.UUID

@Entity
class Attachment(
    @ManyToOne(   fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", referencedColumnName = "message_id", nullable = false)
    var message:Message?=null,
    @Lob
    var attachment:ByteArray?=null,
    var contentType:String,
    var fileName:String
) {
    @Id
    @GeneratedValue(generator = "uuid2")
    var attachment_id:UUID?=null
}