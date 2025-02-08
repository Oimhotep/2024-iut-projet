package iut.nantes.project.products.repository.product

import iut.nantes.project.products.entities.ProductEntity
import iut.nantes.project.products.models.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface ProductRepository  {
    fun save(product: Product): Product

    fun findByFamilyIDAndPriceRange(familyID: UUID?, minPrice:  Double?, maxPrice: Double?): List<Product>

    fun delete(product: Product)

    fun findById(id : UUID) : Product?

    fun drop()
}