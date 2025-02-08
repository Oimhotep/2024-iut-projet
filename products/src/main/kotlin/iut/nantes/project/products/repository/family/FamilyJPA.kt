package iut.nantes.project.products.repository.family

import iut.nantes.project.products.entities.FamilyEntity
import iut.nantes.project.products.models.FamilyDTO
import iut.nantes.project.products.service.toDTO
import iut.nantes.project.products.service.toEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

class FamilyJPA(private val jpa : FamilyJPARepository): FamilyRepository {
    override fun save(family: FamilyDTO): FamilyDTO {
        val familyEntity = family.toEntity()
        val savedEntity = jpa.save(familyEntity)
        return savedEntity.toDTO()
    }

    override fun findAll(): List<FamilyDTO> {
        return jpa.findAll().map { it.toDTO() }
    }

    override fun findByName(name: String): FamilyDTO? {
        return jpa.findByName(name)?.toDTO()
    }

    override fun findById(id: UUID): FamilyDTO? {
        return jpa.findById(id).map { it.toDTO() }.orElse(null)
    }

    override fun existsByName(name: String): Boolean {
        return jpa.existsByName(name)
    }

    override fun delete(family: FamilyDTO) {
        jpa.delete(family.toEntity())
    }

    override fun drop() {
        jpa.deleteAll()
    }

}

interface FamilyJPARepository : JpaRepository<FamilyEntity , UUID>{

    fun findByName(name: String): FamilyEntity?
    fun existsByName(name: String): Boolean
}