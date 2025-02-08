package iut.nantes.project.stores.service



import iut.nantes.project.stores.exception.EntityNotFoundException
import iut.nantes.project.stores.model.ProductDTO
import iut.nantes.project.stores.model.Store
import iut.nantes.project.stores.model.StoreDTO
import iut.nantes.project.stores.repository.StoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import java.util.*

@Service
@Transactional
class StoreService(
    private val storeRepository: StoreRepository,
    private val contactService: ContactService,
    private val webClient: WebClient
) {

    fun createStore(store: Store): Store {
        // Vérifier si le contact existe, sinon le créer
        val contact = store.contact.id?.let { contactService.getContactById(it) }
            ?: contactService.createContact(store.contact)
        val newStore = store.copy(contact = contact)
        return storeRepository.save(newStore)
    }

    fun getAllStores(): List<StoreDTO> {
        return storeRepository.findAll().sortedBy { it.name }
            .map { store ->
                val products = getProducts(store.productIds)
                StoreDTO(
                    id = store.id,
                    name = store.name,
                    contact = store.contact,
                    products = products
                )
            }
    }


    fun getStoreById(id: Long): StoreDTO {
        val store= storeRepository.findById(id).orElseThrow {
            EntityNotFoundException("Magasin avec l'ID $id non trouvé.")
        }
        val products = getProducts(store.productIds)

        return StoreDTO(
            id = store.id,
            name = store.name,
            contact = store.contact,
            products = products
        )

    }

    fun updateStore(id: Long, store: Store): Store {
        val existingStore = storeRepository.findById(id).orElseThrow {
            EntityNotFoundException("Magasin avec l'ID $id non trouvé.")
        }

        val updatedStore = existingStore.copy(
            name = store.name,
            contact = store.contact,
            productIds = store.productIds
        )

        val savedStore = storeRepository.save(updatedStore)

        return savedStore
    }


    fun deleteStore(id: Long) {
        val store = storeRepository.findById(id).orElseThrow {
            EntityNotFoundException("Magasin avec l'ID $id non trouvé.")
        }
        storeRepository.delete(store)
    }



    fun addProduct(storeId: Long, productId: UUID): Store {
        val existingStore = storeRepository.findById(storeId).orElseThrow {
            EntityNotFoundException("Magasin avec l'ID $storeId non trouvé.")
        }

        // Convertir la liste immutable en mutable, ajouter l'ID et recréer l'objet
        val updatedProductIds = existingStore.productIds.toMutableList().apply { add(productId) }
        val updatedStore = existingStore.copy(productIds = updatedProductIds)

        // Sauvegarde du magasin avec le produit ajouté
        storeRepository.save(updatedStore)

        // Récupérer le produit ajouté via WebClient
        return updatedStore
    }



    fun getProducts(productIds: List<UUID>): List<ProductDTO> {
        return productIds.mapNotNull { productId ->
            try {
                webClient.get()
                    .uri("http://localhost:8080/api/v1/products/$productId") // URL du service Product
                    .retrieve()
                    .bodyToMono(ProductDTO::class.java)
                    .block() // Bloque jusqu'à la réponse (sinon utiliser du réactif)
            } catch (e: Exception) {
                null // En cas d'erreur, on ignore le produit
            }
        }
    }

}
