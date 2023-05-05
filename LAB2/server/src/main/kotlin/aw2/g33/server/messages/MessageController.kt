package aw2.g33.server.messages

import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.tickets.TicketDTO
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*



@RestController
@CrossOrigin
class MessageController(private val messageService: MessageService){

    @PutMapping("/API/Message/")
    @ResponseStatus(HttpStatus.OK)
    fun sendMessage(@RequestBody json:ObjectNode) {
        val ticket:TicketDTO
        val writer:ProfileDTO
        val text:String
        val numberOfAttachment:Int


            try {
                ticket = jacksonObjectMapper().readValue(json.get("ticket").toString())
                writer = jacksonObjectMapper().readValue(json.get("writer").toString())
                text= json.get("text").toString()
                numberOfAttachment=json.get("numberOfAttachment").asInt()
            } catch (ex: Exception) {
                throw MessageBodyException("PUT request at /API/Message/{text} has an incorrect body format")
            }
            messageService.sendMessage(text, ticket, writer,numberOfAttachment)

    }
    @GetMapping("/API/Message/")
    @ResponseStatus(HttpStatus.OK)
    fun receiveAllMessagesByTicket(@RequestBody ticket: TicketDTO):List<Message>
    {
        return messageService.receiveAllMessagesByTicket(ticket)
    }
}