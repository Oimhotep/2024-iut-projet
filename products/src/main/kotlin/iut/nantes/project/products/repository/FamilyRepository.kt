package iut.nantes.project.products.repository

import iut.nantes.project.products.entities.FamilyEntity
import org.hibernate.type.descriptor.jdbc.JdbcTypeFamilyInformation
import java.util.UUID

interface FamilyRepository {
    fun save(family: FamilyEntity) : FamilyEntity
    fun findByName(name : String) : FamilyEntity?
    fun findAll() : List<FamilyEntity>
    fun deleteById(id : UUID)
}