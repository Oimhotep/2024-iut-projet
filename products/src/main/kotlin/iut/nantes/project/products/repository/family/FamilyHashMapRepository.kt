package iut.nantes.project.products.repository.family

import iut.nantes.project.products.models.FamilyDTO
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.*

@Component
@Profile("dev")
class FamilyHashMapRepository : FamilyRepository {
        private val families = mutableMapOf<UUID, FamilyDTO>()

        override fun save(family: FamilyDTO): FamilyDTO {
            this.families[family.id] = family
            return family
        }

        override fun findAll(): List<FamilyDTO> = this.families.values.toList()

        override fun findByName(name: String): FamilyDTO? = this.families.values.find { it.name == name }

        override fun findById(id: UUID): FamilyDTO? {
            return this.families[id]
        }

        override fun existsByName(name: String): Boolean {
            return this.families.values.any { it.name.equals(name, ignoreCase = true) }
        }

        override fun delete(family: FamilyDTO) {
            this.families.remove(family.id)
        }

        override fun drop() {
            this.families.clear()
        }
    }
