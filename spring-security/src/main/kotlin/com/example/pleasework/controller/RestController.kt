package com.example.pleasework.controller

import com.example.pleasework.dao.Person
import com.example.pleasework.service.PersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class RestController {
    @Autowired
    lateinit var personService: PersonService

    @PreAuthorize("hasAuthority('ROLE_API')")
    @PostMapping("/add")
    fun addPerson(@RequestBody person: Person) {
        personService.addPerson(person)
    }

    @PreAuthorize("hasAuthority('ROLE_API')")
    @GetMapping("/list")
    fun getPersonList(@RequestParam(required = false) name: String?,
                      @RequestParam(required = false) address: String?) =
        personService.getPersonList(name, address)

    @PreAuthorize("hasAuthority('ROLE_API')")
    @GetMapping("/{id}/view")
    fun getPerson(@PathVariable("id") id: String) = personService.getPerson(id)

    @PreAuthorize("hasAuthority('ROLE_API')")
    @PostMapping("/{id}/edit")
    fun updatePerson(@RequestBody person: Person, @PathVariable("id") id: String): Person? {
        personService.updatePerson(person, id)
        return personService.getPerson(id)
    }

    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    @DeleteMapping("/{id}/delete")
    fun deletePerson(@PathVariable("id") id: String) = personService.deletePerson(id)
}