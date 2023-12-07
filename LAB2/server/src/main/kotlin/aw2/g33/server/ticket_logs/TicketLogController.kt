package aw2.g33.server.ticket_logs

import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
class TicketLogController(private val ticketLogService:TicketLogService) {

    @PreAuthorize("hasAuthority('ROLE_Manager')")
    @GetMapping("/API/ticketLog/getAll")
    @ResponseStatus(HttpStatus.OK)
    fun getAll():List<TicketLogDTO>{
        return ticketLogService.getAll()
    }
}