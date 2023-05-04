package aw2.g33.server.tickets


import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.profiles.toProfile
import aw2.g33.server.ticket_logs.TicketLogService
import org.springframework.stereotype.Service

@Service
class TicketServiceImpl (private val ticketRepository: TicketRepository,private val ticketLogService: TicketLogService):TicketService {
    override fun create_issue(description: String, customer: ProfileDTO): TicketDTO {
        var ticketToCreate=Ticket(description,customer.toProfile())
        ticketLogService.addToLog(ticketToCreate,ticketToCreate.status);
        ticketRepository.save(ticketToCreate);
        return ticketToCreate.toDTO();
    }

    override fun close_issue(ticket: TicketDTO): TicketDTO {
        if(ticket.ticket_id==null)
            throw ServiceWithNullParams("ticket value cannot be null")
        var ticketOptional = ticketRepository.findById(ticket.ticket_id);
        if(!ticketOptional.isEmpty){
            var ticketToUpdate=ticketOptional.get();
            if(ticketToUpdate.status!=="CLOSED"){
                ticketToUpdate.status="CLOSED"
                ticketLogService.addToLog(ticketToUpdate,ticketToUpdate.status);
                ticketRepository.save(ticketToUpdate);
                return ticketToUpdate.toDTO();
            }
            else{
                throw StatusTransitionIncorrect("Cannot make the required operation, in order to close an issue the status cannot be CLOSED")
            }


        }
        throw PrimaryKeyNotFoundException("Cannot find ticket in DB, ticket_id not found!")
    }

    override fun resolve_issue(ticket: TicketDTO): TicketDTO {
        if(ticket.ticket_id==null)
            throw ServiceWithNullParams("ticket value cannot be null")
        var ticketOptional = ticketRepository.findById(ticket.ticket_id);
        if(!ticketOptional.isEmpty){
            var ticketToUpdate=ticketOptional.get();
            if(ticketToUpdate.status!=="RESOLVED" && ticketToUpdate.status!=="CLOSED"){
                ticketToUpdate.status="RESOLVED"
                ticketLogService.addToLog(ticketToUpdate,ticketToUpdate.status);
                ticketRepository.save(ticketToUpdate);
                return ticketToUpdate.toDTO();
            }
            else{
                throw StatusTransitionIncorrect("Cannot make the required operation, in order to resolve an issue the status has to be OPEN or REOPEN or IN PROGRESS")
            }

        }

        throw PrimaryKeyNotFoundException("Cannot find ticket in DB, ticket_id not found!")
    }

    override fun start_progress(ticket: TicketDTO, worker: ProfileDTO, priority: Int): TicketDTO {
        if(ticket.ticket_id==null)
            throw ServiceWithNullParams("ticket value cannot be null")
        if(worker==null)
            throw ServiceWithNullParams("worker value cannot be null")


        var ticketOptional = ticketRepository.findById(ticket.ticket_id);
        if(!ticketOptional.isEmpty){
            var ticketToUpdate=ticketOptional.get();
            if(ticketToUpdate.status=="OPEN" || ticketToUpdate.status=="REOPEN"){
                ticketToUpdate.status="IN PROGRESS"
                ticketToUpdate.worker=worker.toProfile()
                ticketToUpdate.priority=priority
                ticketLogService.addToLog(ticketToUpdate,ticketToUpdate.status);
                ticketRepository.save(ticketToUpdate);
                return ticketToUpdate.toDTO();
            }
            else{
                throw StatusTransitionIncorrect("Cannot make the required operation, in order to start progress the status has to be OPEN or REOPEN")
            }


        }

        throw PrimaryKeyNotFoundException("Cannot find ticket in DB, ticket_id not found!")
    }

    override fun stop_progress(ticket: TicketDTO): TicketDTO {
        if(ticket.ticket_id==null)
            throw ServiceWithNullParams("ticket value cannot be null")
        var ticketOptional = ticketRepository.findById(ticket.ticket_id);
        if(!ticketOptional.isEmpty){
            var ticketToUpdate=ticketOptional.get();
            if(ticketToUpdate.status=="IN PROGRESS"){
                ticketToUpdate.status="OPEN"
                ticketLogService.addToLog(ticketToUpdate,ticketToUpdate.status);
                ticketRepository.save(ticketToUpdate);
                return ticketToUpdate.toDTO();
            }
            else{
                throw StatusTransitionIncorrect("Cannot make the required operation, in order to stop a process the status has to be INPROGRESS")
            }


        }
        throw PrimaryKeyNotFoundException("Cannot find ticket in DB, ticket_id not found!")
    }

    override fun reopen_issue(ticket: TicketDTO): TicketDTO {
        if(ticket.ticket_id==null)
            throw ServiceWithNullParams("ticket value cannot be null")
        var ticketOptional = ticketRepository.findById(ticket.ticket_id);
        if(!ticketOptional.isEmpty){
            var ticketToUpdate=ticketOptional.get();
            if(ticketToUpdate.status!=="REOPENED" && ticketToUpdate.status!=="IN PROGRESS" && ticketToUpdate.status!=="OPEN"){
                ticketToUpdate.status="REOPENED"
                ticketLogService.addToLog(ticketToUpdate,ticketToUpdate.status);
                ticketRepository.save(ticketToUpdate);
                return ticketToUpdate.toDTO();
            }
            else{
                throw StatusTransitionIncorrect("Cannot make the required operation, in order to reopen a process the status has to be RESOLVED or CLOSED")
            }


        }
        throw PrimaryKeyNotFoundException("Cannot find ticket in DB, ticket_id not found!")

    }

    override fun ticketDTOToTicket(ticketDTO: TicketDTO): Ticket {

            if(ticketDTO?.ticket_id == null)
                throw ServiceWithNullParams("A ticket_id cannot be null")
            if(!ticketRepository.existsById(ticketDTO.ticket_id))
                throw PrimaryKeyNotFoundException("Cannot find ticket in DB, ticket_id not found!")

            return ticketRepository.getReferenceById(ticketDTO.ticket_id);

    }


}