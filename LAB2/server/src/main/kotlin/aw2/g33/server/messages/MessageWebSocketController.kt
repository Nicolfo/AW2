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

    @MessageMapping("/chat.register/{ticketID}")
    @SendTo("/topic/{ticketID}")
    fun register(@DestinationVariable ticketID:UUID, @Payload messageDTO: MessageDTO, headerAccessor: SimpMessageHeaderAccessor): MessageDTO? {
        //headerAccessor.sessionAttributes!!["username"] = messageDTO.sender
        //controllare se username matcha il logged in user e se l'user può far parte della chat (appartiene al ticket)
        //controllare se ticket esiste

        println("Register/Cancel of ${messageDTO.sender}, with message ${messageDTO.type}")

        if(messageService.sendMessage(ticketID,messageDTO,true)!==null) {
            return messageDTO;
        }else{
            return MessageDTO("Error cannot register to this chat!","SYSTEM",MessageDTO.MessageType.ERROR,listOf());
        }
    }

    @MessageMapping("/chat.send/{ticketID}")
    @SendTo("/topic/{ticketID}")
    fun sendMessage(@DestinationVariable ticketID:UUID,@Payload messageDTO: MessageDTO ): MessageDTO? {

           if( messageService.sendMessage(ticketID,messageDTO)!=null){
               return messageDTO
           }
        return MessageDTO("Error cannot send messages to this chat! Verify the ticket status","SYSTEM",MessageDTO.MessageType.ERROR,listOf());

    }

}