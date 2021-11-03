package com.example.pleasework

import com.example.pleasework.dao.Person
import com.example.pleasework.services.PersonService
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class MvcControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var personService: PersonService

    @BeforeAll
    fun setUp() {
        personService.addPerson(Person("Elvina", "Kazan"))
        personService.addPerson(Person("Till", "Berlin"))
    }

    @Test
    fun `should add a person`() {
        mockMvc.perform(get("/app/add"))
            .andExpect(status().isOk)
            .andExpect(view().name("add-form"))
        mockMvc.perform(post("/app/add")
                .param("name", "Christoph")
                .param("address", "Berlin"))
            .andExpect(status().isOk)
            .andExpect(view().name("add-result"))
            .andExpect(model().attributeExists("person"))
            .andExpect(content().string(containsString("Christoph")))
    }

    @Test
    fun `should edit a person`() {
        mockMvc.perform(post("/app/1/edit")
                .param("name", "Elvina")
                .param("address", "Moscow"))
            .andExpect(status().isOk)
            .andExpect(view().name("edit-result"))
            .andExpect(model().attributeExists("person"))
            .andExpect(content().string(containsString("Moscow")))
    }

    @Test
    fun `should get persons list`() {
        mockMvc.perform(get("/app/list"))
            .andExpect(status().isOk)
            .andExpect(view().name("show-all"))
            .andExpect(model().attributeExists("list"))
            .andExpect(content().string(containsString("Elvina")))
    }

    @Test
    fun `should view a person`() {
        mockMvc.perform(get("/app/2/view"))
            .andExpect(status().isOk)
            .andExpect(view().name("view-person"))
            .andExpect(model().attributeExists("person"))
            .andExpect(content().string(containsString("Till")))
    }

    @Test
    fun `should delete a person`() {
        mockMvc.perform(get("/app/1/delete"))
            .andExpect(status().isOk)
            .andExpect(view().name("delete-result"))
    }
}