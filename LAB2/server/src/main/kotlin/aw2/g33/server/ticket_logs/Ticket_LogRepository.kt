package aw2.g33.server.ticket_logs

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util.*
@Repository
interface Ticket_LogRepository : JpaRepository<Ticket_Log, UUID>