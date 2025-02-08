package iut.nantes.project.products.models

import jakarta.persistence.OneToOne
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import org.hibernate.type.descriptor.jdbc.JdbcTypeFamilyInformation
import org.jetbrains.annotations.NotNull
import java.util.*

/**
 * Represente un DTO pour le produit.
 *
 */
data class ProductDTO(
    val id: UUID = UUID.randomUUID(),

    @field:Size(min = 2, max = 20, message = "Name must be between 2 and 20 characters")
    val name: String,

    @field:Size(min = 5, max = 100, message = "Description must be between 5 and 100 characters")
    val description: String,

    @field:NotNull( "Price cannot be null")
    val price: PriceDTO,

    @field:NotNull( "Family cannot be null")
    val familyName: String,
)

/**
 * Represent un DTO pour le prix d'un produit
 */
data class PriceDTO (
    @field:Positive(message = "Amount must be positive")
    val amount : Double,
    @field:Pattern(
        regexp = "^[A-Z]{3}$",
        message = "The currency must consist of exactly 3 uppercase alphabetic characters (e.g., EUR)."
    )
    val currency : String
)

/**
 * Représente un prix avec son montant et sa devise.
 * @param amount : Le montant du prix (doit être positif).
 * @param currency : La devise du prix, composée de trois caractères majuscules (ex : EUR, USD).
 */
data class Price(
    val amount: Double,
    val currency: String
)

/**
 * Représente un produit.
 * @param id : Identifiant unique du produit, généré automatiquement.
 * @param name : Nom du produit.
 * @param description : Description du produit (facultative).
 * @param price : Prix du produit, incluant le montant et la devise.
 * @param familyId : Identifiant de la famille du produit.
 */
data class Product(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String,
    val price: Price,
    val familyId: UUID
)

/**
 * Représente la réponse d'un produit, avec toutes les informations détaillées.
 * Ce modèle est utilisé pour retourner un produit dans les réponses d'API sous le format JSON suivant :
 *
 * {
 *   "id": "a6efa614-0235-4180-bbe7-0ff30f3bb858",
 *   "name": "RC 500",
 *   "description": "VELO ROUTE CYCLOTOURISTE",
 *   "price": {
 *     "amount": 875,
 *     "currency": "EUR"
 *   },
 *   "family": {
 *     "id": "9a852ad2-4b0a-4f74-8e23-5305b733c263",
 *     "name": "Bike",
 *     "description": "All kind of bikes"
 *   }
 * }
 *
 * @param id : Identifiant unique du produit.
 * @param name : Nom du produit.
 * @param description : Description du produit (facultative).
 * @param price : Objet Price contenant le montant et la devise du produit.
 * @param family : Objet Family représentant la famille à laquelle le produit appartient.
 */
data class ProductResponse(
    val id: UUID,
    val name: String,
    val description: String?,
    val price: Price,
    val family: FamilyDTO
)

