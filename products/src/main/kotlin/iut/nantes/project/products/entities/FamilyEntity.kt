package iut.nantes.project.products.entities

import jakarta.persistence.*
import java.util.*


@Entity
@Table(name = "FAMILY")
data class FamilyEntity(
    @Id @GeneratedValue
    @Column(name="family_id")
    val id : UUID = UUID.randomUUID(),
    @Column(unique = true , nullable = false )
    val name : String = "" ,
    val description: String = ""
)
