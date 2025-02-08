package iut.nantes.project.stores.controller

import iut.nantes.project.stores.model.Contact
import iut.nantes.project.stores.model.Address
import iut.nantes.project.stores.service.ContactService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.doNothing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

@ExtendWith(SpringExtension::class)
@WebMvcTest(ContactController::class)
class ContactControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var contactService: ContactService

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `devrait créer un contact`() {
        val contact = Contact(email = "john.doe@example.com", phone =  "0658975635", address =  Address("11 clos des","Nantes","85620"))
        given(contactService.createContact(contact)).willReturn(contact)

        mockMvc.perform(post("/api/v1/contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(contact)))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.email").value("john.doe@example.com"))
            .andExpect(jsonPath("$.phone").value("0658975635"))
            .andExpect(jsonPath("$.address.street").value("11 clos des"))
            .andExpect(jsonPath("$.address.city").value("Nantes"))
            .andExpect(jsonPath("$.address.postalCode").value("85620"))
    }


}
