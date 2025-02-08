package iut.nantes.project.stores.service

import iut.nantes.project.stores.exception.EntityNotFoundException
import iut.nantes.project.stores.model.Store
import iut.nantes.project.stores.repository.StoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class StoreService(
    private val storeRepository: StoreRepository,
    private val contactService: ContactService
) {

    fun createStore(store: Store): Store {
        // Vérifier si le contact existe, sinon le créer
        val contact = store.contact.id?.let { contactService.getContactById(it) }
            ?: contactService.createContact(store.contact)
        val newStore = store.copy(contact = contact)
        return storeRepository.save(newStore)
    }

    fun getAllStores(): List<Store> {
        return storeRepository.findAll().sortedBy { it.name }
    }

    fun getStoreById(id: Long): Store {
        return storeRepository.findById(id).orElseThrow {
            EntityNotFoundException("Magasin avec l'ID $id non trouvé.")
        }
    }

    fun updateStore(id: Long, store: Store): Store {
        val existingStore = getStoreById(id)
        val updatedStore = existingStore.copy(name = store.name, contact = store.contact)
        return storeRepository.save(updatedStore)
    }

    fun deleteStore(id: Long) {
        val store = getStoreById(id)
        storeRepository.delete(store)
    }
}
