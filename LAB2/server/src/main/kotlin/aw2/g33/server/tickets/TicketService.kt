package aw2.g33.server.tickets

import aw2.g33.server.profiles.ProfileDTO

interface TicketService {
    //lista metodi
    fun create_issue(description:String,customer:ProfileDTO):TicketDTO
    fun close_issue(ticket: TicketDTO):TicketDTO
    fun resolve_issue(ticket:TicketDTO):TicketDTO
    fun start_progress(ticket: TicketDTO,worker: ProfileDTO,priority:Int):TicketDTO
    fun stop_progress(ticket: TicketDTO):TicketDTO
    fun reopen_issue(ticket:TicketDTO):TicketDTO
    fun ticketDTOToTicket(ticketDTO: TicketDTO):Ticket
}