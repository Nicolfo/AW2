package aw2.g33.server.messages
import aw2.g33.server.tickets.TicketService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import java.util.UUID

@CrossOrigin(maxAge = 3600)
@Controller
class MessageWebSocketController (var messagingTemplate: SimpMessagingTemplate, var messageService: MessageService,var ticketService: TicketService){
    //var oldMessages:MutableMap<String,MutableList<Message>> = hashMapOf()

    @MessageMapping("/chat.register/{chatId}")
    @SendTo("/topic/{chatId}")
    fun register(@DestinationVariable ticketID:UUID, @Payload messageDTO: MessageDTO, headerAccessor: SimpMessageHeaderAccessor): MessageDTO? {
        headerAccessor.sessionAttributes!!["username"] = messageDTO.getSender()
        //controllare se username matcha il logged in user e se l'user può far parte della chat (appartiene al ticket)
        //controllare se ticket esiste

        println("Register/Cancel of ${messageDTO.getSender()}, with message ${messageDTO.getType()}")
        messageService.sendMessage(ticketID,messageDTO)
        return messageDTO;
    }

    @MessageMapping("/chat.send/{chatId}")
    @SendTo("/topic/{chatId}")
    fun sendMessage(@DestinationVariable ticketID:UUID,@Payload messageDTO: MessageDTO ): MessageDTO? {

        messageService.sendMessage(ticketID,messageDTO)
        //save old messages to DB


        return messageDTO
    }


    /*@MessageMapping("/chat.old/{chatId}")

    fun getOldMessages(@DestinationVariable chatId:String,@Payload chatMessage:ChatMessage?){
        println("Message received")
        oldMessages[chatId]?.forEach { chatMessage -> messagingTemplate.convertAndSend("/topic/$chatId", chatMessage) }

    }*/


}