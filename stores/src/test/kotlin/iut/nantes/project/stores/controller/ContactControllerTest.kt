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
import iut.nantes.project.stores.exception.EntityNotFoundException

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
    @Test
    fun `devrait retourner 400 si l'email est invalide`() {
        val invalidContact = Contact(email = "john.doeexample.com", phone = "0658975635", address = Address("11 clos des", "Nantes", "85620"))

        mockMvc.perform(post("/api/v1/contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidContact)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `devrait retourner 400 si le téléphone est invalide`() {
        val invalidContact = Contact(email = "john.doe@example.com", phone = "06589", address = Address("11 clos des", "Nantes", "85620"))

        mockMvc.perform(post("/api/v1/contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidContact)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `devrait retourner 400 si la rue est invalide`() {
        val invalidContact = Contact(email = "john.doe@example.com", phone = "0658975635", address = Address("4", "Nantes", "85620"))

        mockMvc.perform(post("/api/v1/contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidContact)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `devrait retourner 400 si la ville est invalide`() {
        val invalidContact = Contact(email = "john.doe@example.com", phone = "0658975635", address = Address("11 clos des", "", "85620"))

        mockMvc.perform(post("/api/v1/contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidContact)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `devrait retourner 400 si le code postal est invalide`() {
        val invalidContact = Contact(email = "john.doe@example.com", phone = "0658975635", address = Address("11 clos des", "Nantes", "8562"))

        mockMvc.perform(post("/api/v1/contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidContact)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `devrait récupérer tous les contacts filtrés par ville`() {
        val contacts = listOf(
            Contact(id = 1, email = "john.doe@example.com", phone = "0658975635", address = Address("11 clos des", "Nantes", "85620")),
            Contact(id = 2, email = "jane.doe@example.com", phone = "0645871234", address = Address("22 rue de la paix", "Paris", "75001"))
        )
        given(contactService.getAllContacts("Nantes")).willReturn(listOf(contacts[0]))

        mockMvc.perform(get("/api/v1/contacts?city=Nantes"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
    }

    @Test
    fun `devrait récupérer un contact par id`() {
        val contact = Contact(id = 1, email = "john.doe@example.com", phone = "0658975635", address = Address("11 clos des", "Nantes", "85620"))
        given(contactService.getContactById(1)).willReturn(contact)

        mockMvc.perform(get("/api/v1/contacts/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.email").value("john.doe@example.com"))
            .andExpect(jsonPath("$.phone").value("0658975635"))
            .andExpect(jsonPath("$.address.street").value("11 clos des"))
            .andExpect(jsonPath("$.address.city").value("Nantes"))
            .andExpect(jsonPath("$.address.postalCode").value("85620"))
            .andExpect(jsonPath("$.id").value(1))
    }

    @Test
    fun `devrait récupérer tous les contacts`() {
        val contacts = listOf(
            Contact(id = 1, email = "john.doe@example.com", phone = "0658975635", address = Address("11 clos des", "Nantes", "85620")),
            Contact(id = 2, email = "jane.doe@example.com", phone = "0645871234", address = Address("22 rue de la paix", "Paris", "75001"))
        )
        given(contactService.getAllContacts(null)).willReturn(contacts)

        mockMvc.perform(get("/api/v1/contacts"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
            .andExpect(jsonPath("$[1].email").value("jane.doe@example.com"))
            .andExpect(jsonPath("$[0].phone").value("0658975635"))
            .andExpect(jsonPath("$[1].phone").value("0645871234"))
    }

    @Test
    fun `devrait retourner 404 si le contact n'existe pas`() {
        // Simuler l'exception
        given(contactService.getContactById(1100)).willThrow(EntityNotFoundException("Contact avec l'ID 1100 non trouvé."))

        mockMvc.perform(get("/api/v1/contacts/1100"))
            .andExpect(status().isNotFound)  // Vérifie que le statut est 404
            .andExpect(jsonPath("$.message").value("Contact avec l'ID 1100 non trouvé.")) // Vérifie que le message d'erreur est correct
    }


    @Test
    fun `devrait retourner 400 si l'id est invalide lors de la récupération d'un contact`() {
        mockMvc.perform(get("/api/v1/contacts/abc"))
            .andExpect(status().isBadRequest)
    }


    @Test
    fun `devrait mettre à jour un contact`() {
        val updatedContact = Contact(email = "john.doe.updated@example.com", phone = "0658975636", address = Address("11 clos des", "Nantes", "85620"))
        val contact = Contact(id = 1, email = "john.doe@example.com", phone = "0658975635", address = Address("11 clos des", "Nantes", "85620"))
        given(contactService.updateContact(1, updatedContact)).willReturn(updatedContact)

        mockMvc.perform(put("/api/v1/contacts/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedContact)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.email").value("john.doe.updated@example.com"))
            .andExpect(jsonPath("$.phone").value("0658975636"))
            .andExpect(jsonPath("$.address.street").value("11 clos des"))
            .andExpect(jsonPath("$.address.city").value("Nantes"))
            .andExpect(jsonPath("$.address.postalCode").value("85620"))
    }

    @Test
    fun `devrait retourner 400 si les données sont invalides lors de la mise à jour`() {
        val invalidUpdatedContact = Contact(email = "", phone = "0658975636", address = Address("11 clos des", "Nantes", "85620"))

        mockMvc.perform(put("/api/v1/contacts/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidUpdatedContact)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `devrait supprimer un contact`() {
        doNothing().`when`(contactService).deleteContact(1)

        mockMvc.perform(delete("/api/v1/contacts/1"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `devrait retourner 400 si l'id est invalide lors de la suppression`() {
        mockMvc.perform(delete("/api/v1/contacts/abc"))
            .andExpect(status().isBadRequest)
    }


}
