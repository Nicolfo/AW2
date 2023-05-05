package aw2.g33.server.ticket_logs

import aw2.g33.server.tickets.Ticket
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
class TicketLog (
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "ticketId", referencedColumnName = "ticketId")
    var ticket: Ticket?=null,
    var status:String?=null
){


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    val logId:UUID?=null



    @Column(nullable=false)
    var timeStamp:LocalDateTime= LocalDateTime.now()


}

/*
@Embeddable
class MyKey:Serializable{
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "ticket_id", referencedColumnName = "ticket_id")
    var ticket: Ticket?=null;

    @Column(name="timestamp",nullable=false)
    var time_stamp:LocalDateTime= LocalDateTime.now()

}*/