package aw2.g33.server

import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.profiles.ProfileRepository
import aw2.g33.server.profiles.toDTO
import aw2.g33.server.ticket_logs.TicketLog
import aw2.g33.server.ticket_logs.TicketLogRepository
import aw2.g33.server.tickets.TicketDTO
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.post
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
@Testcontainers
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
class LogTicketsTests {

    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:latest")
            .withDatabaseName("test-db")
            .withUsername("postgres").withPassword("password").withExposedPorts(5432)
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.jpa.hibernate.ddl-auto") {"create-drop"}
        }
    }
    @LocalServerPort
    protected var port: Int = 0
    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var profileRepository: ProfileRepository
    @Autowired
    lateinit var ticketLogRepository: TicketLogRepository

    @WithMockUser(authorities = arrayOf<String>("ROLE_Client","ROLE_Manager"), value = "jacopoclient")
    @Test
    @Order(1)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql","file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addTicket.sql"])
    fun checkLogInfoSwitchStatusTicket(){
        var custumer: ProfileDTO = profileRepository.findById("jacopoclient").get().toDTO()
        var currentTicketStatus:String
        lateinit var response : MvcResult;

        response = mockMvc.post("/API/ticket/create"){
            contentType = MediaType.APPLICATION_JSON
            content = "testLogTickets"
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is2xxSuccessful() } }.andReturn()

        currentTicketStatus ="OPEN"
        var ticketCreated: TicketDTO = jacksonObjectMapper().readValue<TicketDTO>(response.response.getContentAsString())
        var logsTicket:MutableList<TicketLog> = ticketLogRepository.getTicketLogsByTickedIdAndStatus(ticketCreated.ticketId,"OPEN").toMutableList()
        assert(logsTicket.size>0 && logsTicket.get(0).ticket?.ticketId==ticketCreated.ticketId && logsTicket.get(0).ticket?.status.equals(currentTicketStatus)  )

        var workerTicket:ProfileDTO? = profileRepository.findById("jacopoexpert").get().toDTO()
        var bodyRequest:MutableMap<String,Any?>? = mutableMapOf<String,Any?>()

        bodyRequest?.set("ticket",ticketCreated)
        bodyRequest?.set("workerUsername",workerTicket?.username)

        response = mockMvc.post("/API/ticket/start/"+4){
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(bodyRequest)
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is2xxSuccessful() } }.andReturn()

        currentTicketStatus ="IN PROGRESS"
        logsTicket= ticketLogRepository.getTicketLogsByTickedIdAndStatus(ticketCreated.ticketId,currentTicketStatus).toMutableList()
        assert(logsTicket.size>0 && logsTicket.get(0).ticket?.ticketId==ticketCreated.ticketId && logsTicket.get(0).ticket?.status.equals(currentTicketStatus))

        response = mockMvc.post("/API/ticket/stop"){
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(ticketCreated)
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is2xxSuccessful() } }.andReturn()

        currentTicketStatus ="OPEN"
        logsTicket= ticketLogRepository.getTicketLogsByTickedIdAndStatus(ticketCreated.ticketId,currentTicketStatus).toMutableList()
        assert(logsTicket.size>0 && logsTicket.get(logsTicket.size-1).ticket?.ticketId==ticketCreated.ticketId && logsTicket.get(logsTicket.size-1).ticket?.status.equals(currentTicketStatus))

        response = mockMvc.post("/API/ticket/resolve"){
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(ticketCreated)
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is2xxSuccessful() } }.andReturn()

        currentTicketStatus ="RESOLVED"
        logsTicket= ticketLogRepository.getTicketLogsByTickedIdAndStatus(ticketCreated.ticketId,currentTicketStatus).toMutableList()
        assert(logsTicket.size>0 && logsTicket.get(0).ticket?.ticketId==ticketCreated.ticketId && logsTicket.get(0).ticket?.status.equals(currentTicketStatus))

        response = mockMvc.post("/API/ticket/close"){
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(ticketCreated)
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is2xxSuccessful() } }.andReturn()

        currentTicketStatus ="CLOSED"
        logsTicket= ticketLogRepository.getTicketLogsByTickedIdAndStatus(ticketCreated.ticketId,currentTicketStatus).toMutableList()
        assert(logsTicket.size>0 && logsTicket.get(0).ticket?.ticketId==ticketCreated.ticketId && logsTicket.get(0).ticket?.status.equals(currentTicketStatus))

        response = mockMvc.post("/API/ticket/reopen"){
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(ticketCreated)
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is2xxSuccessful() } }.andReturn()

        currentTicketStatus ="REOPENED"
        logsTicket= ticketLogRepository.getTicketLogsByTickedIdAndStatus(ticketCreated.ticketId,currentTicketStatus).toMutableList()
        assert(logsTicket.size>0 && logsTicket.get(0).ticket?.ticketId==ticketCreated.ticketId && logsTicket.get(0).ticket?.status.equals(currentTicketStatus))

    }
}
