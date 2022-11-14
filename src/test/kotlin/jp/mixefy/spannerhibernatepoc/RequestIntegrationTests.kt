package jp.mixefy.spannerhibernatepoc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.Commit

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@TestMethodOrder(OrderAnnotation::class)
class RequestIntegrationTests @Autowired constructor(
    val requestRepository: RequestRepository,
) {

    @Commit
    @Order(1)
    @Test
    fun setUp() {
        requestRepository.save(Request(TEST_COMMAND_LINE))
    }

    @Commit
    @Order(110)
    @Test
    fun `When findAll then return list of Requests`() {
        val found = requestRepository.findAll()
        assertThat(found.size).isGreaterThan(0)
    }

    @Commit
    @Order(120)
    @Test
    fun `Add executionAttempt to request`() {
        val found = requestRepository.findByCommandLine(TEST_COMMAND_LINE)!!
        found.executionAttempts.add(ExecutionAttempt())
        requestRepository.save(found)
    }

    companion object {
        private val log = LoggerFactory.getLogger(RequestIntegrationTests::class.java)
        private const val TEST_COMMAND_LINE = "integ-test"
    }
}
