package tarasov.batch.job.processor

import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component
import tarasov.batch.job.model.Person

@Component
class PersonItemProcessor : ItemProcessor<Person, Person> {

    override fun process(person: Person): Person {
        val firstName = person.firstName.uppercase()
        val lastName = person.lastName.uppercase()
        return Person(firstName, lastName)
    }
}