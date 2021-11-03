package com.example.pleasework.controllers

import com.example.pleasework.dao.Person
import com.example.pleasework.services.PersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
class RestController {
    @Autowired
    lateinit var personService: PersonService

    @PostMapping("/add")
    fun addPerson(@RequestBody person: Person) {
        personService.addPerson(person)
    }

    @GetMapping("/list")
    fun getPersonList(@RequestParam(required = false) name: String?,
                      @RequestParam(required = false) address: String?) =
        personService.getPersonList(name, address)

    @GetMapping("/{id}/view")
    fun getPerson(@PathVariable("id") id: String) = personService.getPerson(id)

    @PostMapping("/{id}/edit")
    fun updatePerson(@RequestBody person: Person, @PathVariable("id") id: String): Person? {
        personService.updatePerson(person, id)
        return personService.getPerson(id)
    }

    @DeleteMapping("/{id}/delete")
    fun deletePerson(@PathVariable("id") id: String) = personService.deletePerson(id)
}
