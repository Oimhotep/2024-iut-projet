package iut.nantes.project.products.controller


import iut.nantes.project.products.models.ProductDTO
import iut.nantes.project.products.models.ProductResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import iut.nantes.project.products.service.ProductService
@RestController
@RequestMapping("api/v1/products")
class ProductController(private val productService: ProductService) {

    @PostMapping
    fun save(@Valid @RequestBody productDTO : ProductDTO) : ResponseEntity<ProductResponse>{
        val savedProduct = productService.saveProduct(productDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct)
    }
    @GetMapping("/{id}")
    fun getProductById(@PathVariable("id") id : UUID) : ResponseEntity<ProductResponse>{
        val product = productService.getProductById(id)
        return ResponseEntity.ok(product)
    }

    @GetMapping
    fun getProducts(
        @RequestParam(required = false )
        familyName: String?,
        @RequestParam(required = false )
        minPrice: Double? ,
        @RequestParam(required = false )
        maxPrice: Double?,

    ) : ResponseEntity<List<ProductResponse>>{
        val products = productService.getProductsByFilter(familyName, minPrice , maxPrice)
        return ResponseEntity.ok(products)
    }

    @PutMapping("/{id}")
    fun updateProduct(@PathVariable("id") id : UUID,@Valid @RequestBody productDTO : ProductDTO) : ResponseEntity<ProductResponse>{
        val updatedProduct = productService.updateProduct(id, productDTO)
        return ResponseEntity.ok(updatedProduct)
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable("id") id : UUID) : ResponseEntity<Void>{
        productService.deleteProductById(id)
        return ResponseEntity.noContent().build()
    }
}