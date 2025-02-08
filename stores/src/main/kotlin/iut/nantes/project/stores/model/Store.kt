package iut.nantes.project.stores.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.*

@Entity
@Table(name = "stores")
data class Store(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    @field:NotBlank(message = "Le nom du magasin est obligatoire.")
    @field:Size(min = 3, max = 30, message = "Le nom du magasin doit avoir entre 3 et 30 caractères.")
    val name: String,

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    val contact: Contact,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "store_products", joinColumns = [JoinColumn(name = "store_id")])
    @Column(name = "product_id", nullable = false)
    val productIds: List<UUID> = emptyList() // Stocke uniquement les IDs des produits

) {
    constructor() : this(null, "", Contact(), emptyList())
}
