package aw2.g33.server.tickets


import aw2.g33.server.profiles.ProfileDTO
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.contains
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class TicketController(private val ticketService: TicketService) {

    @PostMapping("/API/ticket/create")
    @ResponseStatus(HttpStatus.OK)

    fun createIssue(@RequestBody description:String):TicketDTO{
        return ticketService.createIssue(description)
    }

    @PostMapping("/API/ticket/close")
    @ResponseStatus(HttpStatus.OK)
    fun closeIssue(@RequestBody ticket: TicketDTO):TicketDTO{
        return ticketService.closeIssue(ticket)
    }

    @PostMapping("/API/ticket/resolve")
    @ResponseStatus(HttpStatus.OK)
    fun resolveIssue(@RequestBody ticket:TicketDTO):TicketDTO{
        return ticketService.resolveIssue(ticket)
    }
    @PostMapping("/API/ticket/start/{priority}")
    @ResponseStatus(HttpStatus.OK)
    fun startProgress(@RequestBody json:ObjectNode, @PathVariable priority:Int):TicketDTO{
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
    @PostMapping("/API/ticket/stop")
    @ResponseStatus(HttpStatus.OK)
    fun stopProgress(@RequestBody ticket: TicketDTO):TicketDTO{
        return ticketService.stopProgress(ticket)
    }
    @PostMapping("/API/ticket/reopen")
    @ResponseStatus(HttpStatus.OK)
    fun reopenIssue(@RequestBody ticket:TicketDTO):TicketDTO{
        return ticketService.reopenIssue(ticket)
    }
}


