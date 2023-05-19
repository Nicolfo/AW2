package aw2.g33.server

import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.profiles.ProfileRepository
import aw2.g33.server.profiles.toDTO
import aw2.g33.server.tickets.TicketDTO
import aw2.g33.server.tickets.TicketRepository
import aw2.g33.server.tickets.toDTO
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.*

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class TicketsTests {
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
    lateinit var ticketRepository: TicketRepository

    @WithMockUser(authorities = arrayOf<String>("ROLE_Client"), value = "jacopoclient")
    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql"])
    fun openNewTicketWithClient(){
        var custumer:ProfileDTO = profileRepository.findById("jacopoclient").get().toDTO()
        var bodyJSON:String = "Test Ticket"

        var response = mockMvc.post("/API/ticket/create"){
            contentType = MediaType.APPLICATION_JSON
            content = bodyJSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is2xxSuccessful() } }.andReturn()

        var ticketCreated:TicketDTO = jacksonObjectMapper().readValue<TicketDTO>(response.response.getContentAsString())
        assert(ticketCreated.ticketId!=null)
        assert(ticketCreated.description!=null && ticketCreated.description.length>0)
        assert(ticketCreated.customerUsername.equals(custumer.username))
    }

    @WithMockUser(authorities = arrayOf<String>("ROLE_Manager"), value = "jacopoclient")
    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql"])
    fun openNewTicketWithManager(){
        var bodyJSON:String = "Test Ticket"

        mockMvc.post("/API/ticket/create"){
            contentType = MediaType.APPLICATION_JSON
            content = bodyJSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is4xxClientError() } }
    }

    @WithMockUser(authorities = arrayOf<String>("ROLE_Manager"), value = "jacopomanager")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql","file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addTicket.sql"])
    @Test
    fun closeTicketWithManager(){

        var ticketPresent:TicketDTO? = ticketRepository.findAll()?.get(0)?.toDTO()

        var response = mockMvc.post("/API/ticket/close"){
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(ticketPresent)
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is2xxSuccessful() } }.andReturn()

        assertDoesNotThrow { response.response.getContentAsString()!=null}
        assert(ticketPresent!=null && ticketPresent.ticketId!=null && ticketRepository.existsById(ticketPresent.ticketId!!))
    }

    @WithMockUser(authorities = arrayOf<String>("ROLE_Client"), value = "jacopoclient")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql","file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addTicket.sql"])
    @Test
    fun closeTicketWithClient(){

        var ticketPresent:TicketDTO? = ticketRepository.findAll()?.get(0)?.toDTO()

        mockMvc.post("/API/ticket/close"){
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(ticketPresent)
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is4xxClientError() } }

    }

    @WithMockUser(authorities = arrayOf<String>("ROLE_Manager"), value = "jacopomanager")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql","file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addTicket.sql"])
    @Test
    fun resolveTicketWithManager(){

        var ticketPresent:TicketDTO? = ticketRepository.findAll()?.get(0)?.toDTO()

        var response = mockMvc.post("/API/ticket/resolve"){
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(ticketPresent)
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is2xxSuccessful() } }.andReturn()

        assertDoesNotThrow { response.response.getContentAsString()!=null}
        var ticketResponse:TicketDTO = jacksonObjectMapper().readValue<TicketDTO>(response.response.getContentAsString())
        assert(ticketResponse!=null && ticketResponse.ticketId!=null && ticketResponse.ticketId == ticketPresent?.ticketId && ticketResponse.status.equals("RESOLVED"))
    }

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql","file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addTicket.sql"])
    @Test
    fun resolveTicketWithUnAuthenticatedUser(){

        var ticketPresent:TicketDTO? = ticketRepository.findAll()?.get(0)?.toDTO()

        var response = mockMvc.post("/API/ticket/resolve"){
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(ticketPresent)
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is4xxClientError() } }
    }

    @WithMockUser(authorities = arrayOf<String>("ROLE_Client"), value = "jacopoclient")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql","file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addTicket.sql"])
    @Test
    fun resolveTicketWithClient(){

        var ticketPresent:TicketDTO? = ticketRepository.findAll()?.get(0)?.toDTO()

        var response = mockMvc.post("/API/ticket/resolve"){
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(ticketPresent)
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is4xxClientError() } }
    }

    @WithMockUser(authorities = arrayOf<String>("ROLE_Manager"), value = "jacopomanager")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql","file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addTicket.sql"])
    @Test
    fun startProgressTicktWithManager(){

        var priorityTicket: Int = 4
        var ticketPresent:TicketDTO? = ticketRepository.findAll()?.get(0)?.toDTO()
        var workerTicket:ProfileDTO? = profileRepository.findById("jacopoexpert").get().toDTO()
        var bodyRequest:MutableMap<String,Any?>? = mutableMapOf<String,Any?>()

        bodyRequest?.set("ticket",ticketPresent)
        bodyRequest?.set("workerUsername",workerTicket?.username)

        var response = mockMvc.post("/API/ticket/start/"+priorityTicket){
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(bodyRequest)
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is2xxSuccessful() } }.andReturn()

        assertDoesNotThrow { response.response.getContentAsString()!=null}
        var ticketResponse:TicketDTO = jacksonObjectMapper().readValue<TicketDTO>(response.response.getContentAsString())
        assert(ticketResponse!=null && ticketResponse.ticketId!=null && ticketResponse.ticketId == ticketPresent?.ticketId)
        assert(ticketResponse!=null && ticketResponse.priority==priorityTicket)
        assert(ticketResponse!=null && ticketResponse.status.equals("IN PROGRESS") && ticketPresent?.status.equals("OPEN"))
        assert(ticketResponse!=null && ticketResponse.workerUsername.equals(workerTicket?.username))
    }

    @WithMockUser(authorities = arrayOf<String>("ROLE_Client"), value = "jacopoclient")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql","file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addTicket.sql"])
    @Test
    fun startProgressTicktWithClient(){

        var priorityTicket: Int = 4
        var ticketPresent:TicketDTO? = ticketRepository.findAll()?.get(0)?.toDTO()
        var workerTicket:ProfileDTO? = profileRepository.findById("jacopoexpert").get().toDTO()
        var bodyRequest:MutableMap<String,Any?>? = mutableMapOf<String,Any?>()

        bodyRequest?.set("ticket",ticketPresent)
        bodyRequest?.set("workerUsername",workerTicket?.username)

        mockMvc.post("/API/ticket/start/"+priorityTicket){
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(bodyRequest)
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is4xxClientError() } }
    }

    @WithMockUser(authorities = arrayOf<String>("ROLE_Manager"), value = "jacopomanager")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql","file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addTicket.sql"])
    @Test
    fun startProgressTicktWithWorkerTicketNotExpert(){

        var priorityTicket: Int = 4
        var ticketPresent:TicketDTO? = ticketRepository.findAll()?.get(0)?.toDTO()
        var workerTicket:ProfileDTO? = profileRepository.findById("jacopoclient").get().toDTO()
        var bodyRequest:MutableMap<String,Any?>? = mutableMapOf<String,Any?>()

        bodyRequest?.set("ticket",ticketPresent)
        bodyRequest?.set("workerUsername",workerTicket?.username)

        mockMvc.post("/API/ticket/start/"+priorityTicket){
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(bodyRequest)
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is4xxClientError() } }
    }

    @WithMockUser(authorities = arrayOf<String>("ROLE_Manager"), value = "jacopomanager")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql","file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addInProgressTicket.sql"])
    @Test
    fun stopProgressTicktWithManager(){

        var ticketPresent:TicketDTO? = ticketRepository.findAll()?.get(0)?.toDTO()

        var response = mockMvc.post("/API/ticket/stop"){
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(ticketPresent)
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is2xxSuccessful() } }.andReturn()

        assertDoesNotThrow { response.response.getContentAsString()!=null}
        var ticketResponse:TicketDTO = jacksonObjectMapper().readValue<TicketDTO>(response.response.getContentAsString())
        assert(ticketResponse!=null && ticketResponse.ticketId!=null && ticketResponse.ticketId == ticketPresent?.ticketId)
        assert(ticketResponse!=null && ticketPresent?.status.equals("IN PROGRESS") && ticketResponse.status.equals("OPEN"))
    }

    @WithMockUser(authorities = arrayOf<String>("ROLE_Manager"), value = "jacopomanager")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql","file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addClosedTicket.sql"])
    @Test
    fun reOpenProgressTicktWithManager(){

        var ticketPresent:TicketDTO? = ticketRepository.findAll()?.get(0)?.toDTO()

        var response = mockMvc.post("/API/ticket/reopen"){
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(ticketPresent)
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is2xxSuccessful() } }.andReturn()

        assertDoesNotThrow { response.response.getContentAsString()!=null}
        var ticketResponse:TicketDTO = jacksonObjectMapper().readValue<TicketDTO>(response.response.getContentAsString())
        assert(ticketResponse!=null && ticketResponse.ticketId!=null && ticketResponse.ticketId == ticketPresent?.ticketId)
        assert(ticketResponse!=null && ticketPresent?.status.equals("CLOSED") && ticketResponse.status.equals("REOPENED"))
    }
}

