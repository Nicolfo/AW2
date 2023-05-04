package aw2.g33.server.messages

import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.tickets.TicketDTO
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.UUID


@RestController
@CrossOrigin
class MessageController(private val messageService: MessageService){

    @PutMapping("/API/Message/{text}")
    @ResponseStatus(HttpStatus.OK)
    fun sendMessage(@RequestBody json:ObjectNode) {
        var ticket:TicketDTO
        var writer:ProfileDTO
        var text:String;

            try {
                ticket = jacksonObjectMapper().readValue<TicketDTO>(json.get("ticket").toString())
                writer = jacksonObjectMapper().readValue<ProfileDTO>(json.get("writer").toString())
                text= json.get("text").toString()
            } catch (ex: Exception) {
                throw MessageBodyException("PUT request at /API/Message/{text} has an incorrect body format")
            }
            messageService.sendMessage(text, ticket, writer);

    }





    @PutMapping("/API/Message/")
    @ResponseStatus(HttpStatus.OK)
    fun sendMessageWithoutText(){
        throw MessageParamException("PUT request at /API/Message/ need a param")
    }
    @GetMapping("/API/Message/")
    @ResponseStatus(HttpStatus.OK)
    fun receiveAllMessagesByTicket(@RequestBody ticket: TicketDTO):List<Message>
    {
        return messageService.receiveAllMessagesByTicket(ticket);
    }
}