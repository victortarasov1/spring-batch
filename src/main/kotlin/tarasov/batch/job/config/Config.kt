package tarasov.batch.job.config

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import tarasov.batch.job.listener.JobCompletionNotificationListener
import tarasov.batch.job.model.Person
import tarasov.batch.job.processor.PersonItemProcessor
import javax.sql.DataSource


@Configuration
class Config {

    @Bean
    fun flatFileItemReader() = FlatFileItemReaderBuilder<Person>().apply {
        name("personItemReader")
        resource(ClassPathResource("sample-data.csv"))
        delimited().names("firstName", "lastName")
        targetType(Person::class.java)
    }.build()
    
    @Bean
    fun writer(dataSource: DataSource) = JdbcBatchItemWriterBuilder<Person>().apply {
        sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
        dataSource(dataSource)
        beanMapped()

    }.build()


    @Bean
    fun firstStep(jobRepository: JobRepository, transactionManager: DataSourceTransactionManager,
                  reader: FlatFileItemReader<Person>, processor: PersonItemProcessor, writer: JdbcBatchItemWriter<Person>): Step {
        return StepBuilder("step1", jobRepository)
                .chunk<Person, Person>(3, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build()
    }

    @Bean
    fun importUserJob(jobRepository: JobRepository, step: Step, listener: JobCompletionNotificationListener): Job {
        return JobBuilder("importUserJob", jobRepository)
                .listener(listener)
                .start(step)
                .build()
    }
}