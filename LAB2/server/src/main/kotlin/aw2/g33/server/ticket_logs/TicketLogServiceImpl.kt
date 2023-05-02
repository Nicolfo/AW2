package aw2.g33.server.ticket_logs

import aw2.g33.server.tickets.Ticket
import org.springframework.stereotype.Service

@Service
class TicketLogServiceImpl(private val ticketLogRepository: TicketLogRepository):TicketLogService {
    override fun addToLog(ticket: Ticket,status:String) {
        val ticketLog=TicketLog(ticket,status);                                     //cambiare in DTO + ticketDTO.toTicket ?
        ticketLogRepository.save(ticketLog);
    }
}