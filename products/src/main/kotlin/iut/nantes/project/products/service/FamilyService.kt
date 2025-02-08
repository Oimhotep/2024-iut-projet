package iut.nantes.project.products.service

import iut.nantes.project.products.errors.ConflictException
import iut.nantes.project.products.errors.NotFoundException
import iut.nantes.project.products.models.FamilyDTO
import iut.nantes.project.products.repository.family.FamilyRepository
import org.springframework.stereotype.Component

import java.util.*


@Component
class FamilyService(
       private  val familyRepository: FamilyRepository
){
   //private  fun getRepository() = if (activeProfile == "dev") familyHashMapRepository else familyJPA
   // private fun getRepository() = familyHashMapRepository
    fun saveFamily(familyDTO : FamilyDTO) : FamilyDTO {
        val existingFamily = familyRepository.findByName(familyDTO.name)
        if (existingFamily != null )   throw ConflictException("Name conflict: ${familyDTO.name}")
        val newfamily = FamilyDTO(name = familyDTO.name, description = familyDTO.description,)
        return familyRepository.save(newfamily)

    }

    fun findFamilyById(id: UUID) : FamilyDTO {
        return familyRepository.findById(id)?:throw NotFoundException("Family not found")



    }
    fun updateFamily(id : UUID , name : String , description : String) : FamilyDTO {
        val existingFamily = familyRepository.findById(id)?:throw NotFoundException("Family not found")

        if (this.familyRepository.existsByName(name) && existingFamily.name != name) {
            throw IllegalStateException("Name already in use")
        }

        val updatedFamily = FamilyDTO(id, name, description)
        return this.familyRepository.save(updatedFamily)

    }
    fun findAllFamilies(): List<FamilyDTO> {
        return familyRepository.findAll()
    }

   /* fun deleteFamily(id : UUID) {
        val family = this.familyRepository.findById(id)
            ?: throw NoSuchElementException("Family with ID $id not found")
        if (this.productRepository.findByFilter(family.id).isNotEmpty()) {
            throw IllegalStateException("Family is still in use")
        }
        this.familyRepository.delete(family)

    }*/
}