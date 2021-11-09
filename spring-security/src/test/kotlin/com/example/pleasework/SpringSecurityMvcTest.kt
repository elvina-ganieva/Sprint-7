package com.example.pleasework

import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@AutoConfigureMockMvc
class SpringSecurityMvcTest {

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
    fun `should add a person`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/add")
                .param("name", "Elvina")
                .param("address", "Kazan")
        )
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("add-result"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("person"))
            .andExpect(content().string(containsString("Elvina")))
    }

    @Test
    fun `should redirect anonymous user from app-add`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/add")
                .param("name", "Elvina")
                .param("address", "Kazan")
        )
            .andExpect(status().is3xxRedirection)
    }

    @WithMockUser
    @Test
    fun `should get persons list`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/app/list"))
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("show-all"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("list"))
    }

    @Test
    fun `should redirect anonymous user from app-list`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/app/list")
        )
            .andExpect(status().is3xxRedirection)
    }

    @WithMockUser
    @Test
    fun `should view a person`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/add")
                .param("name", "Elvina")
                .param("address", "Kazan")
        )

        mockMvc.perform(MockMvcRequestBuilders.get("/app/1/view"))
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("view-person"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("person"))
            .andExpect(content().string(containsString("Elvina")))
    }

    @Test
    fun `should redirect anonymous user from app-view`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/app/1/view")
        )
            .andExpect(status().is3xxRedirection)
    }

    @WithMockUser
    @Test
    fun `should edit a person`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/add")
                .param("name", "Elvina")
                .param("address", "Kazan")
        )
        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/1/edit")
                .param("name", "Elvina")
                .param("address", "Moscow")
        )
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("edit-result"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("person"))
            .andExpect(content().string(containsString("Moscow")))
    }

    @Test
    fun `should redirect anonymous user from app-edit`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/app/1/edit")
        )
            .andExpect(status().is3xxRedirection)
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
    fun `should not have authorities to delete a user`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/app/1/delete")
        )
            .andExpect(status().isForbidden)
    }
}