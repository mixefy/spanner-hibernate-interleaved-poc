package jp.mixefy.spannerhibernatepoc

import com.google.cloud.spanner.hibernate.Interleaved
import org.hibernate.annotations.Type
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import java.util.*
import javax.persistence.*

@SpringBootApplication
class SpannerHibernatePocApplication

fun main(args: Array<String>) {
    runApplication<SpannerHibernatePocApplication>(*args)
}

@Entity
class Request(

    var commandLine: String,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "requestId", nullable = false, updatable = false)
    var executionAttempts: MutableList<ExecutionAttempt> = mutableListOf(),

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    var requestId: UUID? = null,
)

@Entity
@Interleaved(parentEntity = Request::class, cascadeDelete = true)
class ExecutionAttempt(
    var result: String? = null,
    var attemptedAt: Instant? = null,

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    var attemptId: UUID? = null,
)

interface RequestRepository : JpaRepository<Request, UUID> {

    fun findByCommandLine(commandLine: String): Request?
}
