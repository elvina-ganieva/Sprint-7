package com.example.pleasework

import com.example.pleasework.dao.Person
import com.example.pleasework.services.PersonService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var personService: PersonService

    private val headers = HttpHeaders()

    private fun url(s: String): String {
        return "http://localhost:${port}/${s}"
    }

    @BeforeAll
    fun setUp() {
        personService.addPerson(Person("Elvina", "Kazan"))
        personService.addPerson(Person("Till", "Berlin"))
    }

    @Test
    fun `should redirect app addPerson to login`() {
        val resp = restTemplate.exchange(url("app/add"), HttpMethod.POST,
            HttpEntity<Person>(Person("Christoph", "Berlin"), headers),
            Person::class.java)
        Assertions.assertEquals(resp.statusCode, HttpStatus.FOUND)
    }

    @Test
    fun `should redirect app getList to login`() {
        val resp = restTemplate.exchange(url("app/list"), HttpMethod.GET,
            HttpEntity(null, headers), String::class.java)
        Assertions.assertTrue(resp.body!!.contains("Login page"))
    }

    @Test
    fun `should redirect app viewPerson to login`() {
        val resp = restTemplate.exchange(url("app/1/view"),
            HttpMethod.GET, HttpEntity<Person>(null, headers),
            String::class.java)
        Assertions.assertTrue(resp.body!!.contains("Login page"))
    }

    @Test
    fun `should redirect api getList to login`() {
        val resp = restTemplate.exchange(url("api/list"), HttpMethod.GET,
            HttpEntity(null, headers),
            String::class.java)
        Assertions.assertTrue(resp.body!!.contains("Login page"))
    }
}