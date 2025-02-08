package iut.nantes.project.products

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class ProductsApplication

fun main(args: Array<String>) {
    runApplication<ProductsApplication>(*args)
}
