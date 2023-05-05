package aw2.g33.server.messages

import aw2.g33.server.attachment.Attachment
import aw2.g33.server.profiles.Profile
import aw2.g33.server.tickets.Ticket
import jakarta.persistence.*


@Entity
class Message (
    @ManyToOne
    @JoinColumn(name = "ticket_id", referencedColumnName = "ticket_id")
    val ticket: Ticket? = null,
    val text:String="",
    @OneToOne
    @JoinColumn(name = "writer_email", referencedColumnName = "email")
    val writer: Profile? = null,
    val numberOfAttachment:Int
){
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var message_id:Long?=null

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "message")
    val attachments = mutableListOf<Attachment>();


}