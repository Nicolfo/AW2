package aw2.g33.server.ticket_logs

import aw2.g33.server.tickets.Ticket
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Entity
class Ticket_Log {
   /* @Id
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "ticket_id", referencedColumnName = "ticket_id")
    var ticket: Ticket?=null;

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    var time_stamp:LocalDateTime= LocalDateTime.now()*/
   /* @EmbeddedId
    var myKey=MyKey();
*/

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    val log_id:UUID?=null

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "ticket_id", referencedColumnName = "ticket_id")
    var ticket: Ticket?=null

    @Column(name="timestamp",nullable=false)
    var time_stamp:LocalDateTime= LocalDateTime.now()

    var status:String?=null
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