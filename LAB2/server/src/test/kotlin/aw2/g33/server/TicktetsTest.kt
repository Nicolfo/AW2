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


    @Test
    @Order(1)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["file:/Users/jacopospaccatrosi/Desktop/POLI/Web Application II/Lab/Lab 2/AW2/LAB2/server/src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql"])
    fun openNewTicket(){
        var custumer:ProfileDTO = profileRepository.findById("jacopo@studenti.polito.it").get().toDTO()
        var bodyJSON:String = jacksonObjectMapper().writeValueAsString(custumer)

        var response = mockMvc.post("/API/ticket/create/test"){
            contentType = MediaType.APPLICATION_JSON
            content = bodyJSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is2xxSuccessful() } }.andReturn()

        var ticketCreated:TicketDTO = jacksonObjectMapper().readValue<TicketDTO>(response.response.getContentAsString())
        assert(ticketCreated.ticketId!=null)
        assert(ticketCreated.description!=null && ticketCreated.description.length>0)
        assert(ticketCreated.customerEmail.equals(custumer.email))
        }

    @Order(2)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["file:/Users/jacopospaccatrosi/Desktop/POLI/Web Application II/Lab/Lab 2/AW2/LAB2/server/src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql","file:/Users/jacopospaccatrosi/Desktop/POLI/Web Application II/Lab/Lab 2/AW2/LAB2/server/src/test/kotlin/aw2/g33/server/sql/ticketTest/addTicket.sql"])
    @Test
    fun closeTicket(){

        var ticketPresent:TicketDTO? = ticketRepository.findAll()?.get(0)?.toDTO()

        var response = mockMvc.post("/API/ticket/close"){
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(ticketPresent)
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is2xxSuccessful() } }.andReturn()

//        var response = mockMvc.get("/API/ticket/close"){
//            contentType = MediaType.APPLICATION_JSON
//        }.andExpect { status { is2xxSuccessful() } }.andReturn()

        assertDoesNotThrow { response.response.getContentAsString()!=null}
        assert(ticketPresent!=null && ticketPresent.ticketId!=null && ticketRepository.existsById(ticketPresent.ticketId!!))
    }

    @Order(3)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["file:/Users/jacopospaccatrosi/Desktop/POLI/Web Application II/Lab/Lab 2/AW2/LAB2/server/src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql","file:/Users/jacopospaccatrosi/Desktop/POLI/Web Application II/Lab/Lab 2/AW2/LAB2/server/src/test/kotlin/aw2/g33/server/sql/ticketTest/addTicket.sql"])
    @Test
    fun resolveTicket(){

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

    @Order(4)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["file:/Users/jacopospaccatrosi/Desktop/POLI/Web Application II/Lab/Lab 2/AW2/LAB2/server/src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql","file:/Users/jacopospaccatrosi/Desktop/POLI/Web Application II/Lab/Lab 2/AW2/LAB2/server/src/test/kotlin/aw2/g33/server/sql/ticketTest/addTicket.sql"])
    @Test
    fun startProgressTickt(){

        var priorityTicket: Int = 4
        var ticketPresent:TicketDTO? = ticketRepository.findAll()?.get(0)?.toDTO()
        var workerTicket:ProfileDTO? = profileRepository.findById("jacopo@studenti.polito.it").get().toDTO()
        var bodyRequest:MutableMap<String,Any?>? = mutableMapOf<String,Any?>()

        bodyRequest?.set("ticket",ticketPresent)
        bodyRequest?.set("worker",workerTicket)

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
        assert(ticketResponse!=null && ticketResponse.workerEmail.equals(workerTicket?.email))
    }

    @Order(5)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["file:/Users/jacopospaccatrosi/Desktop/POLI/Web Application II/Lab/Lab 2/AW2/LAB2/server/src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql","file:/Users/jacopospaccatrosi/Desktop/POLI/Web Application II/Lab/Lab 3/AW2/LAB2/server/src/test/kotlin/aw2/g33/server/sql/ticketTest/addInProgressTicket.sql"])
    @Test
    fun stopProgressTickt(){

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

    @Order(6)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["file:/Users/jacopospaccatrosi/Desktop/POLI/Web Application II/Lab/Lab 3/AW2/LAB2/server/src/test/kotlin/aw2/g33/server/sql/ticketTest/addInProgressTicket.sql"])
    @Test
    fun reOpenProgressTickt(){

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

