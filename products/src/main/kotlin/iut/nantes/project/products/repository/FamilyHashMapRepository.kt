package iut.nantes.project.products.repository

import iut.nantes.project.products.entities.FamilyEntity
import org.hibernate.type.descriptor.jdbc.JdbcTypeFamilyInformation
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.*

@Component
@Profile("dev")
class FamilyHashMapRepository : FamilyRepository {
    private val  familyMap = mutableMapOf<UUID, FamilyEntity>()

    override fun save(familyEntity: FamilyEntity) : FamilyEntity {
        familyMap[familyEntity.id] = familyEntity
        return familyEntity
    }

    override fun findByName(name: String) : FamilyEntity? {
        return familyMap.values.find { it.name == name }
    }

    override fun findAll() : List<FamilyEntity> {
        return familyMap.values.toList()
    }

    override fun deleteById(id : UUID) {
        familyMap.remove(id)
    }
}