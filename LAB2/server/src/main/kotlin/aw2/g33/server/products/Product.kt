package aw2.g33.server.products

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name="products")
class Product {
    @Id
    val productId: String=""
    val name: String=""
    val brand: String=""
}