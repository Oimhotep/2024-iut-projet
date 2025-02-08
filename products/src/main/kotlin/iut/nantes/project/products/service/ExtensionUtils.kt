package iut.nantes.project.products.service

import iut.nantes.project.products.entities.FamilyEntity
import iut.nantes.project.products.entities.PriceEntity
import iut.nantes.project.products.entities.ProductEntity
import iut.nantes.project.products.models.*

import java.util.*

fun FamilyEntity.toDTO() = FamilyDTO(id = id?:UUID.randomUUID() , name, description)
fun FamilyDTO.toEntity() = FamilyEntity(id= id ,name , description)
fun ProductEntity.toDTO() = Product(id = id?:UUID.randomUUID(), name , description, Price(this.price.amount , this.price.currency), this.familyId  )
fun Product.toEntity() = ProductEntity(UUID.randomUUID() , name , description , PriceEntity(this.price.amount , this.price.currency), this.familyId )
fun Product.toProductResponse(product : Product , family : FamilyDTO): ProductResponse = ProductResponse(
    id = product.id,
    name = product.name,
    description = product.description,
    price = Price(
        amount = product.price.amount,
        currency = product.price.currency,

    ),
    family = FamilyDTO(
        id = family.id,
        name = family.name,
        description = family.description,
    )
)
fun PriceEntity.toDTO() = PriceDTO(amount , currency)
fun PriceDTO.toEntity() = PriceEntity(amount , currency)