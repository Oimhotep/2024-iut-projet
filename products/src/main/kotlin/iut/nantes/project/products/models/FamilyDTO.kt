package iut.nantes.project.products.models

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class FamilyDTO (
    val id : UUID = UUID.randomUUID() ,
    val name : String ,
    val description : String
)

