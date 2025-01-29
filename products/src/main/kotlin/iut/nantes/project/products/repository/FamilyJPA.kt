package iut.nantes.project.products.repository

import iut.nantes.project.products.entities.FamilyEntity
import jakarta.persistence.*
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
@Profile("!dev")
interface  FamilyJPA :
    JpaRepository<FamilyEntity,UUID> , FamilyRepository {
        override fun findByName(name: String): FamilyEntity?
    }
