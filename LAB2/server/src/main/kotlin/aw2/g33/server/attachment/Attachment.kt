package aw2.g33.server.attachment

import aw2.g33.server.messages.Message
import jakarta.persistence.*
import java.util.UUID

@Entity
class Attachment {
    @Id
    @GeneratedValue(generator = "uuid2")
    var attachment_id:UUID?=null
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "message_id", referencedColumnName = "message_id", nullable = false)
    var message:Message?=null

    //var attachment:ByteArray
    //campo blob
}