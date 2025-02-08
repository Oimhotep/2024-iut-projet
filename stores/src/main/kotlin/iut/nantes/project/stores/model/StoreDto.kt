package iut.nantes.project.stores.model



data class StoreDTO(
    val id: Long?,
    val name: String,
    val contact: Contact,
    val products: List<ProductDTO> // Contient les détails des produits
)

data class ProductDTO(
    val id: Long?,
    val name: String,
)
