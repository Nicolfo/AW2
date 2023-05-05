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
                text= json.get("text").asText()
                numberOfAttachment=json.get("numberOfAttachment").asInt()
            } catch (ex: Exception) {
                throw MessageBodyException("PUT request at /API/Message/{text} has an incorrect body format")
            }
            messageService.sendMessage(text, ticket, writer,numberOfAttachment)

    }

    @PostMapping("/API/Message/")
    @ResponseStatus(HttpStatus.OK)
    fun sendMessageWithAttachmentsAsByteArray(@RequestBody json:ObjectNode) {
        val ticket:TicketDTO
        val writer:ProfileDTO
        val text:String
        val files:Array<ByteArray>
        val filesName:Array<String>
        val filesType:Array<String>


        try {
            ticket = jacksonObjectMapper().readValue(json.get("ticket").toString())
            writer = jacksonObjectMapper().readValue(json.get("writer").toString())
            text= json.get("text").asText()
            files=jacksonObjectMapper().readValue(json.get("files").toString())
            filesName=jacksonObjectMapper().readValue(json.get("files").toString())
            filesType=jacksonObjectMapper().readValue(json.get("files").toString())
        } catch (ex: Exception) {
            throw MessageBodyException("PUT request at /API/Message/{text} has an incorrect body format")
        }
        if(files.size!=filesName.size || files.size!=filesType.size){
            throw MessageBodyException("PUT request at /API/Message/{text} has an incorrect body format, arrays of different sizes")
        }

        messageService.sendMessageWithAttachments(text, ticket, writer,files,filesName,filesType)

    }
    @GetMapping("/API/Message/{ticket}")
    @ResponseStatus(HttpStatus.OK)
    fun receiveAllMessagesByTicket(@PathVariable ticket: UUID):List<Message>
    {

        return messageService.receiveAllMessagesByTicket(ticket)
    }
}
