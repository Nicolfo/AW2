package aw2.g33.server.tickets


import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.profiles.RequestParamException
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.contains
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class TicketController(private val ticketService: TicketService) {
    //da validare tutti gli input
    @PostMapping("/API/ticket/create/{description}")
    @ResponseStatus(HttpStatus.OK)
    fun create_issue(@PathVariable description:String, @RequestBody customer: ProfileDTO):TicketDTO{
        // TODO: ("inserire gestione errori e validazione input")
        return ticketService.create_issue(description,customer);
    }
    @PostMapping("/API/ticket/create/")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun createIssueWithNoParam(){
        throw aw2.g33.server.tickets.RequestParamException("POST request at /API/ticket/create/ must include a description as param")
    }
    @PostMapping("/API/ticket/close")
    @ResponseStatus(HttpStatus.OK)
    fun close_issue(@RequestBody ticket: TicketDTO):TicketDTO{
        // TODO: ("inserire gestione errori e validazione input")
        return ticketService.close_issue(ticket);
    }

    @PostMapping("/API/ticket/resolve")
    @ResponseStatus(HttpStatus.OK)
    fun resolve_issue(@RequestBody ticket:TicketDTO):TicketDTO{
        // TODO: ("inserire gestione errori e validazione input")
        return ticketService.resolve_issue(ticket);
    }
    @PostMapping("/API/ticket/start/{priority}")
    @ResponseStatus(HttpStatus.OK)
    fun start_progress(@RequestBody json:ObjectNode,@PathVariable priority:Int):TicketDTO{
        // TODO: ("inserire gestione errori e validazione input")
        if(!json.contains("ticket")||!json.contains("worker")) {
            throw RequestBodyException("POST request must contain a worker and a ticket field")
        }
        var ticket:TicketDTO;
        var worker:ProfileDTO;
        try{
            ticket=jacksonObjectMapper().readValue(json.get("ticket").toString())
        }
        catch(ex:Exception){
            throw RequestBodyException("ticket field must contain all the ticket info")
        }
        try {
            worker = jacksonObjectMapper().readValue(json.get("worker").toString())
        }catch (ex:Exception){
            throw RequestBodyException("worker field must contain all the profile info")
        }

        return ticketService.start_progress(ticket,worker,priority);
    }
    @PostMapping("/API/ticket/start/")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun startProgressWithNoParam(){
        throw aw2.g33.server.tickets.RequestParamException("POST request at /ticket/start/ must include a Priority as param")
    }
    @PostMapping("/API/ticket/stop")
    @ResponseStatus(HttpStatus.OK)
    fun stop_progress(@RequestBody ticket: TicketDTO):TicketDTO{
        // TODO: ("inserire gestione errori e validazione input")
        return ticketService.stop_progress(ticket);
    }
    @PostMapping("/API/ticket/reopen")
    @ResponseStatus(HttpStatus.OK)
    fun reopen_issue(@RequestBody ticket:TicketDTO):TicketDTO{
        // TODO: ("inserire gestione errori e validazione input")
        return ticketService.reopen_issue(ticket);
    }
}


