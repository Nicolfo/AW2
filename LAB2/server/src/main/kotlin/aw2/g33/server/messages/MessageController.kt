package aw2.g33.server.messages

import aw2.g33.server.attachment.Attachment
import aw2.g33.server.products.ProductDTO
import aw2.g33.server.profiles.Profile
import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.tickets.Ticket
import aw2.g33.server.tickets.TicketDTO
import aw2.g33.server.tickets.TicketService
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class MessageController(private val messageService: MessageService){

    @PutMapping("/API/Message/{text}")
    @ResponseStatus(HttpStatus.OK)
    fun sendMessage(@PathVariable text:String, @RequestBody json: ObjectNode){
        // TODO: ("inserire gestione errori e validazione input")
        var ticket: TicketDTO = jacksonObjectMapper().readValue<TicketDTO>(json.get("ticket").toString())
        var writer= jacksonObjectMapper().readValue<ProfileDTO>(json.get("writer").toString())
        var attachments= jacksonObjectMapper().readValue<List<Attachment>>(json.get("attachments").toString())
        messageService.sendMessage(text,attachments,ticket,writer);
    }

    @GetMapping("/API/Message/")
    @ResponseStatus(HttpStatus.OK)
    fun reciveAllMessagesByTicket(@RequestBody ticket: TicketDTO):List<Message>
    {
        return messageService.reciveAllMessagesByTicket(ticket);
    }
}