package aw2.g33.server.messages


import aw2.g33.server.messages.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository :JpaRepository<Message,Long>