package aw2.g33.server.tickets

import aw2.g33.server.profiles.ProfileDTO

interface TicketService {
    //lista metodi
    fun createIssue(description:String):TicketDTO
    fun closeIssue(ticket: TicketDTO):TicketDTO
    fun resolveIssue(ticket:TicketDTO):TicketDTO
    fun startProgress(ticket: TicketDTO, workerUsername: String, priority:Int):TicketDTO
    fun stopProgress(ticket: TicketDTO):TicketDTO
    fun reopenIssue(ticket:TicketDTO):TicketDTO
    fun ticketDTOToTicket(ticketDTO: TicketDTO):Ticket
    fun getListTicketByStatus(statusTicket:String):List<TicketDTO>
    fun getListTicketByUsername():List<TicketDTO>

}
