package iut.nantes.project.stores.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
@Table(name = "stores") // Utilisation du pluriel pour la table
data class Store(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // L'ID est auto-généré par la base de données
    val id: Long? = null,

    @Column(nullable = false)
    @field:NotBlank(message = "Le nom du magasin est obligatoire.")
    @field:Size(min = 3, max = 30, message = "Le nom du magasin doit avoir entre 3 et 30 caractères.")
    val name: String,

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    val contact: Contact // Relation avec le modèle Contact
) {
    constructor() : this(null, "", Contact())
}
