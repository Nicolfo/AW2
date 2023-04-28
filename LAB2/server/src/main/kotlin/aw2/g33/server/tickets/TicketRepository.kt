package aw2.g33.server.tickets

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TicketRepository : JpaRepository<Ticket, UUID>