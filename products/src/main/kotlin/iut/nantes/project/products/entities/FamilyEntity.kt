package iut.nantes.project.products.entities

import jakarta.persistence.*
import java.util.*


@Entity
@Table(name = "FAMILY")
data class FamilyEntity(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="family_id")
    var id : UUID?=null ,
    @Column(unique = true , nullable = false )
    var name : String = "",
    var description: String = "",
)

