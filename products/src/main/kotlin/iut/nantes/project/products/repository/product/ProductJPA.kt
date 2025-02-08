package iut.nantes.project.products.repository.product

import iut.nantes.project.products.entities.ProductEntity
import iut.nantes.project.products.models.Product
import iut.nantes.project.products.service.toDTO
import iut.nantes.project.products.service.toEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

class ProductJPA(private val jpa: ProductJpaRepository) : ProductRepository {

    override fun save(product: Product): Product {
        val productEntity = product.toEntity()
        val savedEntity = jpa.save(productEntity)
        return savedEntity.toDTO()
    }

    override fun findByFamilyIDAndPriceRange(familyID: UUID?, minPrice: Double?, maxPrice: Double?): List<Product> {
        val results = jpa.findAll().filter { productEntity ->
            val matchesFamily = familyID?.let { it == productEntity.familyId } ?: true
            val matchesMinPrice = minPrice?.let { it <= productEntity.price.amount } ?: true
            val matchesMaxPrice = maxPrice?.let { it >= productEntity.price.amount } ?: true

            matchesFamily && matchesMinPrice && matchesMaxPrice
        }
        return results.map { it.toDTO() }
    }

    override fun delete(product: Product) {
        jpa.delete(product.toEntity())
    }



    override fun findById(id: UUID): Product? {
        return jpa.findById(id).orElse(null)?.toDTO()
    }

    override fun drop() {
        jpa.deleteAll()
    }
}

interface ProductJpaRepository : JpaRepository<ProductEntity, UUID>