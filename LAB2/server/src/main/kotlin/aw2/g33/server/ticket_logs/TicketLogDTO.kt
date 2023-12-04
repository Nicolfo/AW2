package aw2.g33.server.ticket_logs

import java.time.LocalDateTime
import java.util.*

data class TicketLogDTO (
    var ticketId: UUID?=null,
    var status:String?=null,
    val logId:UUID?=null,
    var timeStamp: LocalDateTime? =null
)
fun TicketLog.toDTO(): TicketLogDTO {
    return TicketLogDTO(logId=this.logId,ticketId=this.ticket!!.ticketId,status=this.status,timeStamp=this.timeStamp)
}