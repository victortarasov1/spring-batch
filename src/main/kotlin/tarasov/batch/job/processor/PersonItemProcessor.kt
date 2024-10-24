package tarasov.batch.job.processor

import org.springframework.batch.item.ItemProcessor
import tarasov.batch.job.model.Person

class PersonItemProcessor : ItemProcessor<Person, Person> {

    override fun process(person: Person): Person {
        val firstName = person.firstName.uppercase()
        val lastName = person.lastName.uppercase()
        return Person(firstName, lastName)
    }
}