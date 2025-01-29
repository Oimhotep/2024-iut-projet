package iut.nantes.project.products.repository

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

interface  ProductJPA : JpaRepository<ProductEntity, Int>


@Entity
@Table(name ="PRODUCTS")
data class ProductEntity(
    @Id
    @GeneratedValue
    @Column(name= "product_id")
    val id : Int? ,

)
