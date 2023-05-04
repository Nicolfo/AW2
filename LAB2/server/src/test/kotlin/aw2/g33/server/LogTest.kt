package aw2.g33.server

import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.profiles.ProfileRepository
import aw2.g33.server.tickets.TicketDTO
import aw2.g33.server.tickets.TicketRepository
import aw2.g33.server.tickets.toDTO
import aw2.g33.server.tickets.toTicket
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
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.util.*

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@AutoConfigureMockMvc
class LogTests {
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
    //@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,scripts = ["classpath:/sql/profiles.sql"])
    fun openNewTicket(){
        var custumer:ProfileDTO = ProfileDTO("test@email.com","Test New Ticket")
        var bodyJSON:String = jacksonObjectMapper().writeValueAsString(custumer)

        var response = mockMvc.post("/API/ticket/create/test"){
            contentType = MediaType.APPLICATION_JSON
            content = bodyJSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is2xxSuccessful() } }.andReturn()

        var ticketCreated:TicketDTO = jacksonObjectMapper().readValue<TicketDTO>(response.response.getContentAsString())
        assert(ticketCreated.ticket_id!=null)
        assert(ticketCreated.description!=null && ticketCreated.description.length>0)
        assert(ticketCreated.customer_email.equals(custumer.email))
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
        assert(ticketPresent!=null && ticketPresent.ticket_id!=null && ticketRepository.existsById(ticketPresent.ticket_id!!))

    }

}

