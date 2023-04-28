package aw2.g33.server.tickets

import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.profiles.ProfileService
import aw2.g33.server.profiles.toProfile
import org.springframework.stereotype.Service

@Service
class TicketServiceImpl (private val ticketRepository: TicketRepository):TicketService {
    override fun create_issue(description: String, customer: ProfileDTO): TicketDTO {
        var ticket_to_create=Ticket(description,customer.toProfile());
        ticketRepository.save(ticket_to_create);
        return ticket_to_create.toDTO();
        TODO("Not yet implemented")
    }

    override fun close_issue(ticket: TicketDTO): TicketDTO {
        TODO("Not yet implemented")
    }

    override fun resolve_issue(ticket: TicketDTO): TicketDTO {
        TODO("Not yet implemented")
    }

    override fun start_progress(ticket: TicketDTO, worker: ProfileDTO, priority: Int): TicketDTO {
        TODO("Not yet implemented")
    }

    override fun stop_progress(ticket: TicketDTO): TicketDTO {
        TODO("Not yet implemented")
    }

    override fun reopen_issue(ticket: TicketDTO): TicketDTO {
        TODO("Not yet implemented")
    }

}