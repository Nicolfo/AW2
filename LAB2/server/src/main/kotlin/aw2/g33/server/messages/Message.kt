package aw2.g33.server.messages

import aw2.g33.server.attachment.Attachment
import aw2.g33.server.profiles.Profile
import aw2.g33.server.tickets.Ticket
import jakarta.persistence.*


@Entity
class Message (
    @ManyToOne
    @JoinColumn(name = "ticketId", referencedColumnName = "ticketId")
    val ticket: Ticket? = null,
    val content:String="",
    @OneToOne
    @JoinColumn(name = "writer_username", referencedColumnName = "username")
    val sender: Profile? = null,
    val type:String,
){
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var messageId:Long?=null

    @OneToMany(fetch = FetchType.LAZY)
    val attachments = mutableListOf<Attachment>()




}