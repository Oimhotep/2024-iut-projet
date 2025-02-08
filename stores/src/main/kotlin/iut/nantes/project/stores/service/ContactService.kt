package iut.nantes.project.stores.service

import iut.nantes.project.stores.model.Contact
import iut.nantes.project.stores.exception.EntityNotFoundException
import iut.nantes.project.stores.repository.ContactRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class ContactService(private val contactRepository: ContactRepository) {

    fun createContact(contact: Contact): Contact {
        return contactRepository.save(contact)
    }

    fun getContactById(id: Long): Contact {
        return contactRepository.findById(id).orElseThrow {
            EntityNotFoundException("Contact avec l'ID $id non trouvé.")
        }
    }

    fun getAllContacts(city: String?): List<Contact> {
        return if (city != null) {
            contactRepository.findByAddressCity(city)
        } else {
            contactRepository.findAll()
        }
    }

    @Transactional
    fun updateContact(id: Long, updatedContact: Contact): Contact {
        val existingContact = getContactById(id)
        // Vérification de la contrainte (on ne peut pas changer l'email et le téléphone en même temps)
        if (existingContact.email != updatedContact.email && existingContact.phone != updatedContact.phone) {
            throw IllegalArgumentException("Impossible de modifier l'email et le téléphone en même temps.")
        }

        // Mise à jour des champs
        val contactToUpdate = existingContact.copy(
            email = updatedContact.email,
            phone = updatedContact.phone,
            address = updatedContact.address
        )

        return contactRepository.save(contactToUpdate)
    }

    fun deleteContact(id: Long) {
        val contact = getContactById(id)
        // Ici on vérifiera plus tard s'il est lié à un magasin avant suppression
        contactRepository.delete(contact)
    }
}
