package aw2.g33.server.messages

import aw2.g33.server.attachment.AttachmentDTO
import aw2.g33.server.attachment.toDTO
import aw2.g33.server.profiles.Profile
import aw2.g33.server.profiles.ProfileDTO

class MessageDTO( var content: String? ,
        var sender: String? ,
        var type: MessageType? ,
        var files:List<AttachmentDTO>?) {


    enum class MessageType {
        CHAT, LEAVE, JOIN
    }

    fun getContent(): String? {
        return content
    }

    fun setContent(content: String?) {
        this.content = content
    }

    fun getSender(): String? {
        return sender
    }

    fun setSender(sender: String?) {
        this.sender = sender
    }

    fun getType(): MessageType? {
        return type
    }

    fun setType(type: MessageType?) {
        this.type = type
    }

    fun getFiles():List<AttachmentDTO>?{
        return this.files;
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
        else -> {
            MessageDTO.MessageType.CHAT
        }
    }
    return MessageDTO(this.content,this.sender?.username,type,this.attachments.map(){it.toDTO()})
}
