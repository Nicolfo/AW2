package aw2.g33.server.messages

import aw2.g33.server.attachment.Attachment
import aw2.g33.server.profiles.Profile
import aw2.g33.server.tickets.Ticket
import jakarta.persistence.*
import java.util.*


@Entity
class Message (
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "ticket_id", referencedColumnName = "ticket_id")
    val ticket: Ticket? = null,
    val text:String="",
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "writer_email", referencedColumnName = "email")
    val writer: Profile? = null,
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "message")
    val attachments  :List<Attachment>
){
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var message_id:Long?=null




}