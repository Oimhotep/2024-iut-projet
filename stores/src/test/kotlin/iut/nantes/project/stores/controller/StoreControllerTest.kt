package iut.nantes.project.stores.controller

import iut.nantes.project.stores.controller.StoreController
import iut.nantes.project.stores.model.Store
import iut.nantes.project.stores.service.StoreService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import kotlin.test.assertEquals
import iut.nantes.project.stores.model.Contact

import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import iut.nantes.project.stores.exception.EntityNotFoundException
import iut.nantes.project.stores.model.Address
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@SpringBootTest
@AutoConfigureMockMvc
class StoreControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var storeService: StoreService

    private lateinit var store: Store

    @BeforeEach
    fun setUp() {
        store = Store(name = "Atlantis", contact = mock(Contact::class.java))
        mockMvc = MockMvcBuilders.standaloneSetup(StoreController(storeService)).build()
    }

    @Test
    fun `devrait créer un magasin`() {
        `when`(storeService.createStore(store)).thenReturn(store)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{"name": "Atlantis", "contact": {"id": 1, "email": "email@example.com", "phone": "0123456789", "address": {"street": "Rue Truc", "city": "Nantes", "postalCode": "44300"}}}"""))
            .andExpect { status().isCreated }
            .andExpect { jsonPath("$.name").value("Atlantis") }
    }

    @Test
    fun `devrait retourner 400 si le format de l'id est invalide`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/stores/abc"))
            .andExpect { status().isBadRequest }
    }


    @Test
    fun `devrait retourner 400 si le nom du magasin est trop court`() {
        val store = Store(name = "A", contact = mock(Contact::class.java))

        mockMvc.perform(post("/api/v1/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{
            "name": "A",
            "contact": {"id": 1, "email": "email@example.com", "phone": "0123456789", "address": {"street": "Rue Truc", "city": "Nantes", "postalCode": "44300"}}
        }"""))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `devrait retourner 400 si le contact est nul`() {
        mockMvc.perform(post("/api/v1/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{
            "name": "Atlantis",
            "contact": null
        }"""))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `devrait ignorer la liste de produits lors de la création`() {
        val store = Store(name = "Atlantis", contact = mock(Contact::class.java))

        mockMvc.perform(post("/api/v1/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{
            "name": "Atlantis",
            "contact": {"id": 1, "email": "email@example.com", "phone": "0123456789", "address": {"street": "Rue Truc", "city": "Nantes", "postalCode": "44300"}},
            "products": [{"id": 1, "name": "Product 1"}]
        }"""))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.products").doesNotExist())
    }

    @Test
    fun `devrait récupérer tous les magasins triés par nom`() {
        val store1 = Store(name = "Atlantis", contact = mock(Contact::class.java))
        val store2 = Store(name = "Babel", contact = mock(Contact::class.java))

        `when`(storeService.getAllStores()).thenReturn(listOf(store1, store2))

        mockMvc.perform(get("/api/v1/stores"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("Atlantis"))
            .andExpect(jsonPath("$[1].name").value("Babel"))
    }

    @Test
    fun `devrait récupérer un magasin par son ID`() {
        val store = Store(name = "Atlantis", contact = mock(Contact::class.java))

        `when`(storeService.getStoreById(1L)).thenReturn(store)

        mockMvc.perform(get("/api/v1/stores/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Atlantis"))
    }

    @Test
    fun `devrait retourner 400 si l'ID est invalide`() {
        mockMvc.perform(get("/api/v1/stores/abc"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `devrait retourner 404 si le magasin n'existe pas`() {
        `when`(storeService.getStoreById(999L)).thenThrow(EntityNotFoundException("Magasin avec l'ID 999 non trouvé."))

        mockMvc.perform(get("/api/v1/stores/999"))
            .andExpect(status().isNotFound) // Vérifie que le statut est 404
            .andExpect(jsonPath("$.message").value("Magasin avec l'ID 999 non trouvé."))
    }


    @Test
    fun `devrait mettre à jour un magasin existant`() {
        val updatedStore = Store(name = "New Atlantis", contact = mock(Contact::class.java))

        `when`(storeService.updateStore(1L, updatedStore)).thenReturn(updatedStore)

        mockMvc.perform(put("/api/v1/stores/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{
                "name": "New Atlantis",
                "contact": {"id": 1, "email": "newemail@example.com", "phone": "0123456789", "address": {"street": "Rue Truc", "city": "Nantes", "postalCode": "44300"}}
            }"""))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("New Atlantis"))
    }

    @Test
    fun `devrait retourner 400 si les données sont invalides lors de la mise à jour`() {
        mockMvc.perform(put("/api/v1/stores/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{
            "name": "A",
            "contact": {"id": 1, "email": "invalidemail", "phone": "123", "address": {"street": "Rue Truc", "city": "Nantes", "postalCode": "44300"}}
        }"""))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `devrait supprimer un magasin existant`() {
        mockMvc.perform(delete("/api/v1/stores/1"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `devrait retourner 400 si l'ID est invalide lors de la suppression`() {
        mockMvc.perform(delete("/api/v1/stores/abc"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `devrait retourner 404 si le magasin à supprimer n'existe pas`() {
        `when`(storeService.deleteStore(999L)).thenThrow(EntityNotFoundException("Magasin avec l'ID 999 non trouvé."))

        mockMvc.perform(delete("/api/v1/stores/999"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message").value("Magasin avec l'ID 999 non trouvé."))
    }













    // Ajouter d'autres tests pour les autres endpoints comme PUT, DELETE
}
