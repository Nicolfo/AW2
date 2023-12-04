package aw2.g33.server.ticket_logs

import aw2.g33.server.tickets.Ticket

interface TicketLogService {
    fun addToLog(ticket: Ticket,status:String)
    fun getAll():List<TicketLogDTO>
}