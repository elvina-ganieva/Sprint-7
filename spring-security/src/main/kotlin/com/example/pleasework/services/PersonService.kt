package com.example.pleasework.services

import com.example.pleasework.dao.Person
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap


@Service
class PersonService {

    fun getPersonList(name: String?, address: String?): Map<String, Person> {
        return persons.filterValues { if (name != null) it.name == name else true }
            .filterValues { if (address != null) it.address == address else true }
    }

    fun getPerson(id: String): Person? = persons[id]

    fun addPerson(person: Person) {
        person.id = id
        persons[id.toString()] = person
        id++
    }

    fun updatePerson(person: Person, id: String) {
        person.id = id.toInt()
        persons[id] = person
    }

    fun deletePerson(id: String) = persons.remove(id)

    companion object {
        private val persons = ConcurrentHashMap<String, Person>()
        private var id = 1
    }
}
