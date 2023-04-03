package wa2.g33.server.products

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name="products")
class Product {
    @Id
    var productid: String = ""
    var name: String = ""
    var brand: String = ""
}