package iut.nantes.project.products.repository.product

import iut.nantes.project.products.entities.ProductEntity
import iut.nantes.project.products.models.Product
import org.springframework.context.annotation.Profile
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.query.FluentQuery
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function

@Component
@Profile("dev")
class ProductHashMap : ProductRepository {
    private val products = mutableMapOf<UUID, Product>()

    override fun save(product: Product): Product {
        this.products[product.id] = product
        return product
    }


    override fun findByFamilyIDAndPriceRange(familyID: UUID?, minPrice: Double?, maxPrice: Double?): List<Product> {
        return this.products.values.filter { product ->
            val matchesFamily = familyID?.let { it == product.familyId } ?: true
            val matchesMinPrice = minPrice?.let { it <= product.price.amount } ?: true
            val matchesMaxPrice = maxPrice?.let { it >= product.price.amount } ?: true

            matchesFamily && matchesMinPrice && matchesMaxPrice
        }
    }

    override fun findById(id: UUID): Product? {
        return this.products[id]
    }

    override fun delete(product: Product) {
        this.products.remove(product.id)
    }

    override fun drop() {
        this.products.clear()
    }
}