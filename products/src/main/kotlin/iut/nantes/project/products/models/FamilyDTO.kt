package iut.nantes.project.products.models

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class FamilyDTO (
    val id : UUID? = null ,
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 3 , max = 30 , message="Name must be between 3 and 30 character")

    val name : String,
    @field:NotBlank(message = "Description is required")
    @field:Size(min=5 , max = 100 , message="Description must be between 5 and 100 character")
    val description : String
)

