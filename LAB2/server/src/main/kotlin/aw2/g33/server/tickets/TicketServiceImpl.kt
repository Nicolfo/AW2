package aw2.g33.server.tickets

import aw2.g33.server.messages.Message
import aw2.g33.server.messages.MessageService
import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.profiles.ProfileService
import aw2.g33.server.profiles.toProfile
import aw2.g33.server.ticket_logs.TicketLogService
import org.springframework.stereotype.Service

@Service
class TicketServiceImpl (private val ticketRepository: TicketRepository,private val ticketLogService: TicketLogService,private val messageService: MessageService):TicketService {
    override fun create_issue(description: String, customer: ProfileDTO): TicketDTO {
        var ticket_to_create=Ticket(description,customer.toProfile());
        ticketLogService.addToLog(ticket_to_create,ticket_to_create.status);
        ticketRepository.save(ticket_to_create);
        return ticket_to_create.toDTO();
    }

    override fun close_issue(ticket: TicketDTO): TicketDTO {
        if(ticket.ticket_id==null)
            return ticket;                                         //da generare eccezione
        var ticketOptional = ticketRepository.findById(ticket.ticket_id);
        if(!ticketOptional.isEmpty){
            var ticketToUpdate=ticketOptional.get();
            if(ticketToUpdate.status!=="CLOSED"){
                ticketToUpdate.status="CLOSED"                                          //CONTROLLARE NOME STATUS
                ticketLogService.addToLog(ticketToUpdate,ticketToUpdate.status);
                ticketRepository.save(ticketToUpdate);
                return ticketToUpdate.toDTO();
            }


        }
        return ticket;                                                                  //GENERARE ECCEZIONE
    }

    override fun resolve_issue(ticket: TicketDTO): TicketDTO {
        if(ticket.ticket_id==null)
            return ticket;                                         //da generare eccezione
        var ticketOptional = ticketRepository.findById(ticket.ticket_id);
        if(!ticketOptional.isEmpty){
            var ticketToUpdate=ticketOptional.get();
            if(ticketToUpdate.status!=="RESOLVED" && ticketToUpdate.status!=="CLOSED"){
                ticketToUpdate.status="RESOLVED"
                ticketLogService.addToLog(ticketToUpdate,ticketToUpdate.status);
                ticketRepository.save(ticketToUpdate);
                return ticketToUpdate.toDTO();
            }


        }
        return ticket;                                                                  //GENERARE ECCEZIONE
    }

    override fun start_progress(ticket: TicketDTO, worker: ProfileDTO, priority: Int): TicketDTO {
        if(ticket.ticket_id==null)
            return ticket;                                         //da generare eccezione
        if(worker==null)
            return ticket;                                          //generare eccezione
        var ticketOptional = ticketRepository.findById(ticket.ticket_id);
        if(!ticketOptional.isEmpty){
            var ticketToUpdate=ticketOptional.get();
            if(ticketToUpdate.status=="OPEN" || ticketToUpdate.status=="REOPEN"){
                ticketToUpdate.status="IN PROGRESS"                                          //CONTROLLARE NOME STATUS
                ticketToUpdate.worker=worker.toProfile()
                ticketToUpdate.priority=priority
                //TODO("Creare chat");
                ticketLogService.addToLog(ticketToUpdate,ticketToUpdate.status);
                ticketRepository.save(ticketToUpdate);
                return ticketToUpdate.toDTO();
            }


        }
        return ticket;                                                                  //GENERARE ECCEZIONE
    }

    override fun stop_progress(ticket: TicketDTO): TicketDTO {
        if(ticket.ticket_id==null)
            return ticket;                                         //da generare eccezione
        var ticketOptional = ticketRepository.findById(ticket.ticket_id);
        if(!ticketOptional.isEmpty){
            var ticketToUpdate=ticketOptional.get();
            if(ticketToUpdate.status=="IN PROGRESS"){
                ticketToUpdate.status="OPEN"                                          //CONTROLLARE NOME STATUS
                ticketLogService.addToLog(ticketToUpdate,ticketToUpdate.status);
                ticketRepository.save(ticketToUpdate);
                return ticketToUpdate.toDTO();
            }


        }
        return ticket;                                                                  //GENERARE ECCEZIONE
    }

    override fun reopen_issue(ticket: TicketDTO): TicketDTO {
        if(ticket.ticket_id==null)
            return ticket;                                         //da generare eccezione
        var ticketOptional = ticketRepository.findById(ticket.ticket_id);
        if(!ticketOptional.isEmpty){
            var ticketToUpdate=ticketOptional.get();
            if(ticketToUpdate.status!=="REOPENED" && ticketToUpdate.status!=="IN PROGRESS" && ticketToUpdate.status!=="OPEN"){
                ticketToUpdate.status="REOPENED"                                          //CONTROLLARE NOME STATUS
                ticketLogService.addToLog(ticketToUpdate,ticketToUpdate.status);
                ticketRepository.save(ticketToUpdate);
                return ticketToUpdate.toDTO();
            }


        }
        return ticket;                                                                  //GENERARE ECCEZIONE
    }

}