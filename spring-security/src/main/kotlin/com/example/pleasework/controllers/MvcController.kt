package com.example.pleasework.controllers

import com.example.pleasework.dao.Person
import com.example.pleasework.services.PersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*


@Controller
@RequestMapping("/app")
class MvcController {
    @Autowired
    lateinit var personService: PersonService

    @GetMapping("/add")
    fun addForm(person: Person) = "add-form"

    @PostMapping("/add")
    fun addPerson(@ModelAttribute("person") person: Person, model: Model): String {
        personService.addPerson(person)
        return "add-result"
    }

    @GetMapping("/list")
    fun getPersonList(model: Model,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) address: String?): String {
        model.addAttribute("list", personService.getPersonList(name, address))
        return "show-all"
    }

    @GetMapping("/{id}/view")
    fun getPerson(@PathVariable("id") id: String, model: Model): String {
        model.addAttribute("person", personService.getPerson(id))
        return "view-person"
    }

    @GetMapping("/{id}/edit")
    fun updateForm(@PathVariable("id") id: String, model: Model): String {
        model.addAttribute("person", personService.getPerson(id))
        return "edit-form"
    }

    @PostMapping("/{id}/edit")
    fun updatePerson(@ModelAttribute("person") person: Person,
        @PathVariable("id") id: String): String {
        personService.updatePerson(person, id)
        return "edit-result"
    }

    @GetMapping("/{id}/delete")
    fun deletePerson(@PathVariable("id") id: String, model: Model): String {
        model.addAttribute("person", personService.getPerson(id))
        personService.deletePerson(id)
        return "delete-result"
    }
}
