package aw2.g33.server.tickets


import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.profiles.ProfileService
import aw2.g33.server.profiles.toProfile
import aw2.g33.server.ticket_logs.TicketLogService

import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TicketServiceImpl (private val ticketRepository: TicketRepository,private val ticketLogService: TicketLogService,private val profileService: ProfileService):TicketService {
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_Client')")
    override fun createIssue(description: String): TicketDTO {
        val username= SecurityContextHolder.getContext().authentication.name
        val customer=profileService.getProfileInfo(username)
        val ticketToCreate=Ticket(description,customer.toProfile())
        ticketLogService.addToLog(ticketToCreate,ticketToCreate.status)
        ticketRepository.save(ticketToCreate)

        return ticketToCreate.toDTO()
    }

   @Transactional
   override fun closeIssue(ticketID: UUID): TicketDTO {

        if(ticketID==null)
            throw ServiceWithNullParams("ticket value cannot be null")
        val ticketOptional = ticketRepository.findById(ticketID)
        if(!ticketOptional.isEmpty){
            val ticketToUpdate=ticketOptional.get()
            if(ticketToUpdate.status.compareTo("CLOSED")!=0){
                ticketToUpdate.status="CLOSED"
                ticketLogService.addToLog(ticketToUpdate,ticketToUpdate.status)
                ticketRepository.save(ticketToUpdate)
                return ticketToUpdate.toDTO()
            }
            else{
                throw StatusTransitionIncorrect("Cannot make the required operation, in order to close an issue the status cannot be CLOSED")
            }


        }
        throw PrimaryKeyNotFoundException("Cannot find ticket in DB, ticket_id not found!")
    }
    @Transactional
    override fun resolveIssue(ticketID: UUID): TicketDTO {
        if(ticketID==null)
            throw ServiceWithNullParams("ticket value cannot be null")
        val ticketOptional = ticketRepository.findById(ticketID)
        if(!ticketOptional.isEmpty){
            val ticketToUpdate=ticketOptional.get()
            if(ticketToUpdate.status.compareTo("RESOLVED")!=0 && ticketToUpdate.status.compareTo("CLOSED")!=0){
                ticketToUpdate.status="RESOLVED"
                ticketLogService.addToLog(ticketToUpdate,ticketToUpdate.status)
                ticketRepository.save(ticketToUpdate)
                return ticketToUpdate.toDTO()
            }
            else{
                throw StatusTransitionIncorrect("Cannot make the required operation, in order to resolve an issue the status has to be OPEN or REOPEN or IN PROGRESS")
            }

        }

        throw PrimaryKeyNotFoundException("Cannot find ticket in DB, ticket_id not found!")
    }
    @Transactional
    override fun startProgress(ticket: TicketDTO, workerUsername: String, priority: Int): TicketDTO {
        if(ticket.ticketId==null)
            throw ServiceWithNullParams("ticket value cannot be null")

        val worker:ProfileDTO = profileService.getProfileInfo(workerUsername);
        if(!worker.role.equals("Expert"))
            throw InvalidPermsssionUser("Worker not found...")

        val ticketOptional = ticketRepository.findById(ticket.ticketId)
        if(!ticketOptional.isEmpty){
            val ticketToUpdate=ticketOptional.get()
            if(ticketToUpdate.status.compareTo("OPEN")==0 || ticketToUpdate.status.compareTo("REOPENED")==0){
                ticketToUpdate.status="IN PROGRESS"
                ticketToUpdate.worker=worker.toProfile()
                ticketToUpdate.priority=priority
                ticketLogService.addToLog(ticketToUpdate,ticketToUpdate.status)
                ticketRepository.save(ticketToUpdate)
                return ticketToUpdate.toDTO()
            }
            else{
                throw StatusTransitionIncorrect("Cannot make the required operation, in order to start progress the status has to be OPEN or REOPEN")
            }


        }

        throw PrimaryKeyNotFoundException("Cannot find ticket in DB, ticket_id not found!")
    }
    @Transactional
    override fun stopProgress(ticketID: UUID): TicketDTO {
        if(ticketID==null)
            throw ServiceWithNullParams("ticket value cannot be null")
        val ticketOptional = ticketRepository.findById(ticketID)
        if(!ticketOptional.isEmpty){
            val ticketToUpdate=ticketOptional.get()
            if(ticketToUpdate.status.compareTo("IN PROGRESS")==0){
                ticketToUpdate.status="OPEN"
                ticketToUpdate.worker=null;
                ticketLogService.addToLog(ticketToUpdate,ticketToUpdate.status)
                ticketRepository.save(ticketToUpdate)
                return ticketToUpdate.toDTO()
            }
            else{
                throw StatusTransitionIncorrect("Cannot make the required operation, in order to stop a process the status has to be IN PROGRESS")
            }


        }
        throw PrimaryKeyNotFoundException("Cannot find ticket in DB, ticket_id not found!")
    }
    @Transactional
    override fun reopenIssue(ticketID: UUID): TicketDTO {
        if(ticketID==null)
            throw ServiceWithNullParams("ticket value cannot be null")
        val ticketOptional = ticketRepository.findById(ticketID)
        if(!ticketOptional.isEmpty){
            val ticketToUpdate=ticketOptional.get()
            if(ticketToUpdate.status.compareTo("REOPENED")!=0 && ticketToUpdate.status.compareTo("IN PROGRESS")!=0 && ticketToUpdate.status.compareTo("OPEN")!=0){
                ticketToUpdate.status="REOPENED"
                ticketToUpdate.worker=null;
                ticketLogService.addToLog(ticketToUpdate,ticketToUpdate.status)
                ticketRepository.save(ticketToUpdate)
                return ticketToUpdate.toDTO()
            }
            else{
                throw StatusTransitionIncorrect("Cannot make the required operation, in order to reopen a process the status has to be RESOLVED or CLOSED")
            }


        }
        throw PrimaryKeyNotFoundException("Cannot find ticket in DB, ticket_id not found!")

    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_Manager')")
    override fun getListTicketByStatus(statusTicket:String):List<TicketDTO>{
        if(statusTicket.compareTo("OPEN")!=0)
            return ticketRepository.findTicketsByStatus(statusTicket).map { it -> it.toDTO() }
        else{
            var firstList=ticketRepository.findTicketsByStatus(statusTicket).map { it -> it.toDTO() };
            var secondList=ticketRepository.findTicketsByStatus("REOPENED").map { it -> it.toDTO() }
            return firstList+secondList;
        }
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    override fun getListTicketByUsername():List<TicketDTO>{
        var isManager = SecurityContextHolder.getContext().authentication.authorities.stream().anyMatch{ it -> it.authority == "ROLE_Manager"};
        if(isManager)
            return ticketRepository.findAll().map { it -> it.toDTO() }
        else{
            var username:String = SecurityContextHolder.getContext().authentication.name;
            return ticketRepository.findTicketsByUsername(username).map { it -> it.toDTO() }

        }

    }


    override fun ticketDTOToTicket(ticketDTO: TicketDTO): Ticket {

            if(ticketDTO.ticketId == null)
                throw ServiceWithNullParams("A ticket_id cannot be null")
            if(!ticketRepository.existsById(ticketDTO.ticketId))
                throw PrimaryKeyNotFoundException("Cannot find ticket in DB, ticket_id not found!")

            return ticketRepository.getReferenceById(ticketDTO.ticketId)

    }


}
