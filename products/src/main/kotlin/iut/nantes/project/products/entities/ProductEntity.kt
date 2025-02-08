package iut.nantes.project.products.entities
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import org.hibernate.type.descriptor.jdbc.JdbcTypeFamilyInformation
import org.jetbrains.annotations.NotNull
import java.util.*
@Entity
@Table(name = "PRODUCT")
data class ProductEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id : UUID? = null ,
    @field:Size(min=2 , max =20 , message="Name must be between 2 and 20 characters")
    var name : String= "",


    @field:Size(min=5 , max=100 , message="Description must be between 5 and 100 characters")
    var description : String="",

    @Embedded
    @field:NotNull("Price cannot be null")
    var price : PriceEntity,

    val familyId : UUID

) {
    constructor() : this(null , "" , "" , PriceEntity() , UUID.randomUUID())

}


@Embeddable
data class PriceEntity (
   @Column(nullable = false)
    private var amount_ : Double = 0.0 ,
    @Column(nullable = false  , length = 3)
    private var  currency_ : String= ""

){
    var amount : Double
    get() = amount_
        private set(value) {
            amount_ = value
        }

    var currency: String
    get() = currency_
    private set(value) {
        currency_ = value
    }
}