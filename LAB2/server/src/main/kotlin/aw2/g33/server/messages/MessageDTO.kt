package aw2.g33.server.messages

import aw2.g33.server.attachment.AttachmentDTO
import aw2.g33.server.attachment.toDTO
import aw2.g33.server.profiles.Profile
import aw2.g33.server.profiles.ProfileDTO

class MessageDTO( var content: String? ,
        var sender: String? ,
        var type: MessageType? ,
        var files:List<AttachmentDTO> = listOf()) {


    enum class MessageType {
        CHAT, LEAVE, JOIN,ERROR
    }



    override fun toString(): String {
        return super.toString() + this.sender + this.content + this.files
    }
}
fun Message.toDTO(): MessageDTO {

    var type = when (this.type){
        "CHAT" -> MessageDTO.MessageType.CHAT
        "LEAVE" -> MessageDTO.MessageType.LEAVE
        "JOIN" ->  MessageDTO.MessageType.JOIN
        "ERROR" ->  MessageDTO.MessageType.ERROR
        else -> {
            MessageDTO.MessageType.CHAT
        }
    }
    return MessageDTO(this.content,this.sender?.username,type,this.attachments.map{it.toDTO()})
}
