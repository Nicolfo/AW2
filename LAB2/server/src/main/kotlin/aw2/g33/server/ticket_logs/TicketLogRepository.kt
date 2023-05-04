package aw2.g33.server.ticket_logs

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.util.*
@Repository
interface TicketLogRepository : JpaRepository<TicketLog, UUID>{
    @Query("select * from ticket_log where ticket_id = :ticket_id", nativeQuery = true)
    fun getTicketLogsByTickedId(@Param("ticket_id") ticket_id:UUID?):List<TicketLog>

    @Query("select * from ticket_log where ticket_id = :ticket_id and status=:status", nativeQuery = true)
    fun getTicketLogsByTickedIdAndStatus(@Param("ticket_id") ticket_id:UUID?,@Param("status") status:String ):List<TicketLog>
}
