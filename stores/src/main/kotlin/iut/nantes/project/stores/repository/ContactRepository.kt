package iut.nantes.project.stores.repository

import iut.nantes.project.stores.model.Contact
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContactRepository : JpaRepository<Contact, Long> {
    fun findByAddressCity(city: String): List<Contact> // Permet de récupérer les contacts par ville
}
