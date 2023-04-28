package aw2.g33.server.ticket_logs

import aw2.g33.server.tickets.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*
@Repository
interface Ticket_LogRepository : JpaRepository<Ticket_Log, MyKey>