package com.example.pleasework

import com.example.pleasework.dao.Person
import com.example.pleasework.service.PersonService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringSecurityRestTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    val personService: PersonService = PersonService()

    private val headers = HttpHeaders()

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setUp() {
        val cookie = getCookie("api", "api")
        headers.add("Cookie", cookie)
        personService.addPerson(Person("Elvina", "Kazan"))
        personService.addPerson(Person("Till", "Berlin"))
    }

    @AfterEach
    fun tearDown() {
        personService.deleteAllPersons()
    }

    private fun url(s: String): String {
        return "http://localhost:${port}/${s}"
    }

    fun getCookie(username: String, password: String): String {
        val map: MultiValueMap<String, String> = LinkedMultiValueMap()

        map.set("username", username)
        map.set("password", password)
        val resp: ResponseEntity<String> = restTemplate.postForEntity(
            url("login"),
            HttpEntity(map, HttpHeaders()), String::class.java
        )
        return resp.headers["Set-Cookie"]!![0]
    }

    @Test
    fun `should add a person`() {
        restTemplate.exchange(url("api/add"), HttpMethod.POST,
            HttpEntity<Person>(Person("Christoph", "Berlin"), headers),
            Person::class.java)
        assertEquals("Christoph", personService.getPerson("3")!!.name)
    }

    @Test
    fun `should get persons list`() {
        val resp = restTemplate.exchange(url("api/list"), HttpMethod.GET,
            HttpEntity(null, headers),
            object : ParameterizedTypeReference<Map<String, Person>>() {})
        assertEquals("Elvina", resp.body!!["1"]!!.name)
        assertEquals("Till", resp.body!!["2"]!!.name)
    }

    @Test
    fun `should view a person`() {
        val resp: ResponseEntity<Person> = restTemplate.exchange(url("api/1/view"),
            HttpMethod.GET, HttpEntity<Person>(null, headers),
            Person::class.java)
        assertEquals("Elvina", resp.body!!.name)
    }

    @Test
    fun `should update a person`() {
        val entity = HttpEntity<Person>(Person("Elvina", "Moscow"), headers)
        val resp = restTemplate.exchange(
            url("api/1/edit"), HttpMethod.POST,
            entity, Person::class.java, 1)
        assertEquals("Moscow", resp.body!!.address)
        assertEquals("Elvina", resp.body!!.name)

    }

    @Test
    fun `should not delete a person`() {
        val resp = restTemplate.exchange(url("api/1/delete"),
            HttpMethod.DELETE, HttpEntity(null, headers),
            Person::class.java, 1)
        assertEquals(HttpStatus.FORBIDDEN, resp.statusCode)
    }
}