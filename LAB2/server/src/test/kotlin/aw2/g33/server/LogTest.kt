package aw2.g33.server

import aw2.g33.server.profiles.Profile
import aw2.g33.server.profiles.ProfileRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
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
    lateinit var profileRepository: ProfileRepository
    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,scripts = ["classpath:/sql/profiles.sql"])
    fun someTest() {
        val tmp:List<Profile> = profileRepository.findAll()
        assert(tmp.isEmpty())
    }
}

