package iut.nantes.project.products.service

import iut.nantes.project.products.entities.FamilyEntity
import iut.nantes.project.products.errors.ConflictException
import iut.nantes.project.products.models.FamilyDTO
import iut.nantes.project.products.repository.FamilyHashMapRepository

import iut.nantes.project.products.repository.FamilyJPA
import iut.nantes.project.products.repository.FamilyRepository
import org.hibernate.type.descriptor.jdbc.JdbcTypeFamilyInformation
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

import java.util.*

fun FamilyEntity.toDTO() = FamilyDTO(id = id , name, description)
fun  FamilyDTO.toEntity() = FamilyEntity(id= id?:UUID.randomUUID() ,name , description)
@Component
class FamilyService(
       private  val familyRepository: FamilyRepository
){
   //private  fun getRepository() = if (activeProfile == "dev") familyHashMapRepository else familyJPA
   // private fun getRepository() = familyHashMapRepository
    fun saveFamily(familyDTO : FamilyDTO) : FamilyDTO {
        val existingFamily = familyRepository.findByName(familyDTO.name)
        if (existingFamily != null )   throw ConflictException("Name conflict: ${familyDTO.name}")
        val newfamily = familyDTO.toEntity()
        return familyRepository.save(newfamily).toDTO()

    }

    fun findAllFamilies(): List<FamilyDTO> {
        return familyRepository.findAll().map{it.toDTO()}
    }

    fun deleteFamily(id : UUID) {
        familyRepository.deleteById(id)
    }
}