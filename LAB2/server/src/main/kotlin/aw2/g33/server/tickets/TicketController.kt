package aw2.g33.server.tickets


import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.contains
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@CrossOrigin
class TicketController(private val ticketService: TicketService) {

    @PostMapping("/API/ticket/create")
    @ResponseStatus(HttpStatus.OK)
    fun createIssue(@RequestBody description:String):TicketDTO{
        return ticketService.createIssue(description)
    }

    @PostMapping("/API/ticket/{ticketID}/close")
    @PreAuthorize("hasAuthority('ROLE_Manager') || hasAuthority('ROLE_Expert')")
    @ResponseStatus(HttpStatus.OK)
    fun closeIssue(@PathVariable ticketID: UUID):TicketDTO{
        return ticketService.closeIssue(ticketID)
    }

    @PostMapping("/API/ticket/{ticketID}/resolve")
    @PreAuthorize("hasAuthority('ROLE_Manager') || hasAuthority('ROLE_Expert')")
    @ResponseStatus(HttpStatus.OK)
    fun resolveIssue(@PathVariable ticketID: UUID):TicketDTO{
        return ticketService.resolveIssue(ticketID)
    }
    @PostMapping("/API/ticket/start")
    @ResponseStatus(HttpStatus.OK)
    fun startProgress(@RequestBody json:ObjectNode, @RequestParam(defaultValue = "0") priority:Int):TicketDTO{
         if(!json.contains("ticket")||!json.contains("workerUsername")) {
            throw RequestBodyException("POST request must contain a worker and a ticket field")
        }
        val ticket:TicketDTO
        val workerUsername:String
        try{
            ticket=jacksonObjectMapper().readValue(json.get("ticket").toString())
        }
        catch(ex:Exception){
            throw RequestBodyException("ticket field must contain all the ticket info")
        }
        try {
            workerUsername = jacksonObjectMapper().readValue(json.get("workerUsername").toString())
        }catch (ex:Exception){
            throw RequestBodyException("worker field must contain all the profile info")
        }

        return ticketService.startProgress(ticket, workerUsername ,priority)
    }
    @PostMapping("/API/ticket/start/")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun startProgressWithNoParam(){
        throw RequestParamException("POST request at /ticket/start/ must include a Priority as param")
    }
    @PostMapping("/API/ticket/{ticketID}/stop")
    @PreAuthorize("hasAuthority('ROLE_Manager') || hasAuthority('ROLE_Expert')")
    @ResponseStatus(HttpStatus.OK)
    fun stopProgress(@PathVariable ticketID: UUID):TicketDTO{
        return ticketService.stopProgress(ticketID)
    }
    @PostMapping("/API/ticket/{ticketID}/reopen")
    @PreAuthorize("hasAuthority('ROLE_Manager') || hasAuthority('ROLE_Expert')")
    @ResponseStatus(HttpStatus.OK)
    fun reopenIssue(@PathVariable ticketID: UUID):TicketDTO{
        return ticketService.reopenIssue(ticketID)
    }

    @GetMapping("/API/ticket/getListTicketByStatus")
    @ResponseStatus(HttpStatus.OK)
    fun getListTicketByStatus(@RequestParam(defaultValue = "open") status:String):List<TicketDTO>{
        return ticketService.getListTicketByStatus(status.uppercase(Locale.getDefault()))
    }

    @GetMapping("/API/ticket/getListTicketByUsername")
    @ResponseStatus(HttpStatus.OK)
    fun getListTicketByUsername():List<TicketDTO>{
        return ticketService.getListTicketByUsername();
    }

}


