package tarasov.batch.job.listener

import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import tarasov.batch.job.model.Person


@Component
class JobCompletionNotificationListener(private val jdbcTemplate: JdbcTemplate) : JobExecutionListener {

    override fun afterJob(jobExecution: JobExecution) {
        if (jobExecution.status === BatchStatus.COMPLETED) {
            jdbcTemplate.query("SELECT first_name, last_name FROM people", DataClassRowMapper(Person::class.java))
                    .forEach { person -> println("Found $person in the database. ") }
        }
    }
}