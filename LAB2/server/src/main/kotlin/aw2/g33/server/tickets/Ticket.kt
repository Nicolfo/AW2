package aw2.g33.server.tickets

import aw2.g33.server.messages.Message
import aw2.g33.server.profiles.Profile
import jakarta.persistence.*
import java.util.UUID

@Entity
class Ticket (

    var description:String="",
    @OneToOne
    @JoinColumn(name = "customer_username", referencedColumnName = "username")
    val customer: Profile? = null,
    var status:String="OPEN",
    var priority:Int=-1
){
    @Id
    @GeneratedValue(generator="uuid2")
    var ticketId:UUID?=null

    @OneToOne
    @JoinColumn(name = "worker_username", referencedColumnName = "username")
    var worker:Profile?=null

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "ticket")
    val messages  =mutableListOf<Message>()

}
