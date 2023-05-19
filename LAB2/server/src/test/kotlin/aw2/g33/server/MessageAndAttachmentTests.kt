package aw2.g33.server

import aw2.g33.server.messages.Message
import aw2.g33.server.messages.MessageRepository
import aw2.g33.server.profiles.ProfileDTO
import aw2.g33.server.profiles.ProfileRepository
import aw2.g33.server.profiles.toDTO
import aw2.g33.server.tickets.Ticket
import aw2.g33.server.tickets.TicketDTO
import aw2.g33.server.tickets.TicketRepository
import aw2.g33.server.tickets.toDTO
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class MessageAndAttachmentTests {
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
            registry.add("spring.jpa.hibernate.ddl-auto") { "create-drop" }
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

    @Autowired
    lateinit var messageRepository: MessageRepository

    @WithMockUser(authorities = arrayOf<String>("ROLE_Client","ROLE_Manager"), value = "jacopoclient")
    @Test
    @Order(1)
    @Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = ["file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql", "file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addInProgressTicket.sql"]
    )
    fun sendNewMessage() {

        var ticketMessage: Ticket? = ticketRepository.findAll()?.get(0)
        var ticketMessageDTO: TicketDTO? = ticketRepository.findAll()?.get(0)?.toDTO()
        var custmerTicket: ProfileDTO? = profileRepository.findById("jacopoclient").get().toDTO()
        var bodyRequest: MutableMap<String, Any?>? = mutableMapOf<String, Any?>()
        var textMessage: String = "Test message ..."
        var numberOfAttachment: Int = 0

        bodyRequest?.set("ticket", ticketMessageDTO)
        bodyRequest?.set("writer", custmerTicket)
        bodyRequest?.set("text", textMessage)
        bodyRequest?.set("numberOfAttachment", numberOfAttachment)

        var response = mockMvc.put("/API/Message/") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(bodyRequest)
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is2xxSuccessful() } }.andReturn()

        var messageCreated: Message = messageRepository.findByTicketID(ticketMessage?.ticketId!!).get(0)
        assertDoesNotThrow { messageCreated != null }
        assert(messageCreated.ticket?.ticketId == ticketMessage.ticketId)
        assert(messageCreated.writer?.email == custmerTicket?.email)
        assert(messageCreated.text.equals(textMessage))
        assert(messageCreated.numberOfAttachment == numberOfAttachment)

    }

    @WithMockUser(authorities = arrayOf<String>("ROLE_Client","ROLE_Manager"), value = "jacopoclient")
    @Test
    @Order(2)
    @Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = ["file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addProfile.sql", "file:src/test/kotlin/aw2/g33/server/sql/ticketTest/addInProgressTicket.sql"]
    )
    fun getMessagesForTicket() {

        var ticketMessage: Ticket? = ticketRepository.findAll()?.get(0)
        var ticketMessageDTO: TicketDTO? = ticketRepository.findAll()?.get(0)?.toDTO()
        var custmerTicket: ProfileDTO? = profileRepository.findById("jacopoclient").get().toDTO()
        var bodyRequest: MutableMap<String, Any?>? = mutableMapOf<String, Any?>()
        var textMessage1: String = "Test message ... #1"
        var numberOfAttachment: Int = 0

        bodyRequest?.set("ticket", ticketMessageDTO)
        bodyRequest?.set("writer", custmerTicket)
        bodyRequest?.set("text", textMessage1)
        bodyRequest?.set("numberOfAttachment", numberOfAttachment)

        mockMvc.put("/API/Message/") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(bodyRequest)
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is2xxSuccessful() } }.andReturn()

        var textMessage2: String = "Test message ... #2"
        bodyRequest?.set("text", textMessage2)

        mockMvc.put("/API/Message/") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(bodyRequest)
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is2xxSuccessful() } }.andReturn()

        var response = mockMvc.get("/API/Message/"+ticketMessage?.ticketId.toString()) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect { status { is2xxSuccessful() } }.andReturn()

//        var listMessages:List<Message> = jacksonObjectMapper().readValue<List<Message>>(response.response.getContentAsString())
//        assert(listMessages.size ===2)
//        assert(listMessages.get(0).text === textMessage1)
//        assert(listMessages.get(1).text === textMessage1)
    }

}
