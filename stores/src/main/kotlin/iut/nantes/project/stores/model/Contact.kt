package iut.nantes.project.stores.model

import jakarta.persistence.*
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Entity
@Table(name = "contacts") // Bonne pratique : utiliser le pluriel pour les tables
data class Contact(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Meilleure compatibilité avec H2
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    @field:NotBlank(message = "L'email est obligatoire")
    @field:Email(message = "Le format de l'email est invalide")
    val email: String,

    @Column(nullable = false)
    @field:NotBlank(message = "Le téléphone est obligatoire")
    @field:Pattern(regexp = "\\d{10}", message = "Le téléphone doit contenir exactement 10 chiffres")
    val phone: String,

    @field:Valid
    @Column(nullable = false)
    @Embedded
    val address: Address
) {
    constructor() : this(null, "", "", Address())
}

@Embeddable
data class Address(
    @Column(nullable = false)
    @field:Size(min = 5, max = 50, message = "La rue doit faire entre 5 et 50 caractères")
    val street: String,

    @Column(nullable = false)
    @field:Size(min = 1, max = 30, message = "La ville doit faire entre 1 et 30 caractères")
    val city: String ,

    @Column(nullable = false)
    @field:Pattern(regexp = "\\d{5}", message = "Le code postal doit contenir exactement 5 chiffres")
    val postalCode: String
) {
    constructor() : this("", "", "")
}
