package aw2.g33.server.tickets

import aw2.g33.server.profiles.ProfileDTO

interface TicketService {
    //lista metodi
    fun create_issue(description:String,customer:ProfileDTO):TicketDTO; //da fare come DTO
    fun close_issue(ticket: TicketDTO):TicketDTO; //da fare come DTO
    fun resolve_issue(ticket:TicketDTO):TicketDTO; //da fare come DTO
    fun start_progress(ticket: TicketDTO,worker: ProfileDTO,priority:Int):TicketDTO; //da fare come DTO
    fun stop_progress(ticket: TicketDTO):TicketDTO; //da fare come DTO
    fun reopen_issue(ticket:TicketDTO):TicketDTO; //da fare come DTO
}