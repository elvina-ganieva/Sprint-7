package com.example.pleasework

import org.hamcrest.CoreMatchers.containsString
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class SpringSecurityTest {

    @Autowired
    private lateinit var context: WebApplicationContext

    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()
    }

    @WithMockUser
    @Test
    fun `should add a person through app`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/add")
                .param("name", "Elvina")
                .param("address", "Kazan")
        )
            .andExpect(status().isOk)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/app/list")
        )
            .andExpect(content().string(containsString("Elvina")))
    }

    @Test
    fun `should redirect anonymous user`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/add")
                .param("name", "Elvina")
                .param("address", "Kazan")
        )
            .andExpect(status().is3xxRedirection)
    }

    @WithMockUser(authorities = ["ROLE_API"])
    @Test
    fun `should return a person list through api`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/list")
                .param("name", "Elvina")
                .param("address", "Kazan")
        )
            .andExpect(status().isOk)
    }

    @WithMockUser(authorities = ["ROLE_DELETE"])
    @Test
    fun `should redirect a user without ROLE_API`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/list")
                .param("name", "Elvina")
                .param("address", "Kazan")
        )
            .andExpect(status().isForbidden)
    }

    @WithMockUser(authorities = ["ROLE_DELETE"])
    @Test
    fun `should delete a user`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/add")
                .param("name", "Elvina")
                .param("address", "Kazan")
        )

        mockMvc.perform(
            MockMvcRequestBuilders.get("/app/1/delete")
        )
            .andExpect(status().isOk)
    }

    @WithMockUser
    @Test
    fun `should not delete a user`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/add")
                .param("name", "Elvina")
                .param("address", "Kazan")
        )

        mockMvc.perform(
            MockMvcRequestBuilders.get("/app/1/delete")
        )
            .andExpect(status().isForbidden)
    }
}
