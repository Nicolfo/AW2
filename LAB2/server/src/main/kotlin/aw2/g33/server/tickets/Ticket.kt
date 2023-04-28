package aw2.g33.server.tickets

import aw2.g33.server.profiles.Profile
import jakarta.persistence.*
import java.util.UUID

@Entity
class Ticket (

    var description:String="",
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "customer_email", referencedColumnName = "email")
    val customer: Profile? = null,
    var status:String="OPEN",
    var priority:Int=-1
){
    @Id
    @GeneratedValue(generator="uuid2")
    var ticket_id:UUID?=null



}