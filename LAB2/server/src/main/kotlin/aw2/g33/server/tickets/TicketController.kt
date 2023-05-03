package aw2.g33.server.tickets

import aw2.g33.server.products.ProductDTO
import aw2.g33.server.profiles.ProfileDTO
import com.fasterxml.jackson.databind.node.ObjectNode
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
        var ticket:TicketDTO=jacksonObjectMapper().readValue<TicketDTO>(json.get("ticket").toString())
        var worker=jacksonObjectMapper().readValue<ProfileDTO>(json.get("worker").toString())
        return ticketService.start_progress(ticket,worker,priority);
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


