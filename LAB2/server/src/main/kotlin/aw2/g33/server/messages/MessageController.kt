package aw2.g33.server.messages

import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.tickets.TicketDTO
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.websocket.server.PathParam
import org.springframework.data.repository.query.Param
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@CrossOrigin
class MessageController(private val messageService: MessageService){


    @GetMapping("/API/Message/{ticket}")
    @ResponseStatus(HttpStatus.OK)
    fun receiveAllMessagesByTicket(@PathVariable ticket: UUID):List<MessageDTO>
    {

        return messageService.receiveAllMessagesByTicket(ticket)
    }

}
