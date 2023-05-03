package aw2.g33.server.tickets

import aw2.g33.server.attachment.Attachment
import aw2.g33.server.messages.Message
import aw2.g33.server.profiles.Profile
import jakarta.persistence.*
import java.util.UUID

@Entity
class Ticket (

    var description:String="",
    @OneToOne()
    @JoinColumn(name = "customer_email", referencedColumnName = "email")
    val customer: Profile? = null,
    var status:String="OPEN",
    public var priority:Int=-1
){
    @Id
    @GeneratedValue(generator="uuid2")
    var ticket_id:UUID?=null

    @OneToOne()
    @JoinColumn(name = "worker_email", referencedColumnName = "email")
    var worker:Profile?=null;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "ticket")
    val messages  =mutableListOf<Message>();

}