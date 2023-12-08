package aw2.g33.server.tickets

import aw2.g33.server.profiles.ProfileDTO
import java.util.*

interface TicketService {
    //lista metodi
    fun createIssue(description:String):TicketDTO
    fun closeIssue(ticketID: UUID):TicketDTO
    fun resolveIssue(ticketID: UUID):TicketDTO
    fun startProgress(ticket: TicketDTO, workerUsername: String, priority:Int):TicketDTO
    fun stopProgress(ticketID: UUID):TicketDTO
    fun reopenIssue(ticketID: UUID):TicketDTO
    fun ticketDTOToTicket(ticketDTO: TicketDTO):Ticket
    fun getListTicketByStatus(statusTicket:String):List<TicketDTO>
    fun getListTicketByUsername():List<TicketDTO>

}
