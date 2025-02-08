package iut.nantes.project.products.service


import iut.nantes.project.products.entities.PriceEntity
import iut.nantes.project.products.errors.NotFoundException
import iut.nantes.project.products.models.*
import iut.nantes.project.products.repository.family.FamilyRepository
import iut.nantes.project.products.repository.product.ProductRepository
import iut.nantes.project.products.service.toDTO
import iut.nantes.project.products.service.toEntity
import org.springframework.stereotype.Component
import java.util.*
import kotlin.NoSuchElementException

@Component
class ProductService(
    private val productRepository: ProductRepository,
    private val familyRepository: FamilyRepository
) {

    fun saveProduct(productDTO: ProductDTO): ProductResponse {
       val family = familyRepository.findByName(productDTO.familyName)
           ?:throw  IllegalArgumentException("Product with ${productDTO.familyName} not found")

       val product = Product(
           id = productDTO.id,
           name = productDTO.name,
           description = productDTO.description,
           price = Price(amount = productDTO.price.amount , currency = productDTO.price.currency),
           familyId = family.id,
       )

        val savedProduct = productRepository.save(product)
        return this.toProductResponse(savedProduct , family)
    }

    // ProductService.kt
    fun getProductsByFilter(familyName: String?, minPrice: Double?, maxPrice: Double?): List<ProductResponse> {
        if (familyName != null && familyRepository.findByName(familyName) == null) {
            return emptyList()
        }

        if (minPrice != null && maxPrice != null && minPrice >= maxPrice) {
            throw IllegalArgumentException("Invalid filter minPrice should be inf to maxPrice.")
        }

        var products = this.productRepository.findByFamilyIDAndPriceRange(null, minPrice , maxPrice)
        if (familyName != null) {
            val family = familyName.let {
                this.familyRepository.findByName(it)
            }
            products = this.productRepository.findByFamilyIDAndPriceRange(family?.id, minPrice, maxPrice)
        }
        return products.map {
                product -> val family = this.familyRepository.findById(product.familyId)
            ?: throw IllegalStateException("Family with ${product.familyId} not found")
            this.toProductResponse(product , family)
        }
    }

    fun getProductById(id: UUID): ProductResponse {
        val product = productRepository.findById(id)
            ?:throw NotFoundException("Product with id $id not found")
        val family = familyRepository.findById(product.familyId)
        ?: throw  NotFoundException("Product with id ${product.familyId} not found")

        return this.toProductResponse(product, family)

    }

    fun updateProduct(id: UUID, productDTO: ProductDTO): ProductResponse {
        val productToUpdate = productRepository.findById(id)
            ?:throw  NotFoundException("Product with ${productDTO.familyName} not found")
        val family = familyRepository.findByName(productDTO.familyName)
        ?: throw  IllegalArgumentException("Product with ${productDTO.familyName} not found")

        val updateProduct = productToUpdate.copy(
            name = productDTO.name,
            description = productDTO.description,
            price = Price(amount = productDTO.price.amount , currency = productDTO.price.currency),
            familyId = family.id,
        )

        val savedProduct = productRepository.save(updateProduct)
        return this.toProductResponse(savedProduct , family)
    }

    fun deleteProductById(id: UUID) {
        val product = productRepository.findById(id)
        ?:throw  NotFoundException("Product with id $id not found")
        this.productRepository.delete(product)
     }

    private fun toProductResponse(product: Product, family: FamilyDTO): ProductResponse {
        return ProductResponse(
            id = product.id,
            name = product.name,
            description = product.description,
            price = Price(
                amount = product.price.amount,
                currency = product.price.currency
            ),
            family = FamilyDTO(
                id = family.id,
                name = family.name,
                description = family.description
            )
        )
    }

}