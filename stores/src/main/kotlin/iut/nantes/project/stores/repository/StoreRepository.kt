package iut.nantes.project.stores.repository

import iut.nantes.project.stores.model.Store
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StoreRepository : JpaRepository<Store, Long> {
    fun findByName(name: String): Store?
}
