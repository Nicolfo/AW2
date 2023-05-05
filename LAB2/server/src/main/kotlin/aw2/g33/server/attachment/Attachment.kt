package aw2.g33.server.attachment

import aw2.g33.server.messages.Message
import jakarta.persistence.*
import java.util.UUID

@Entity
class Attachment(
    @ManyToOne(   fetch = FetchType.LAZY)
    @JoinColumn(name = "messageId", referencedColumnName = "messageId", nullable = false)
    var message:Message?=null,
    @Lob
    var attachment:ByteArray?=null,
    var contentType:String,
    var fileName:String,
    var attachmentOrder:Int
) {
    @Id
    @GeneratedValue(generator = "uuid2")
    var attachmentId:UUID?=null

}