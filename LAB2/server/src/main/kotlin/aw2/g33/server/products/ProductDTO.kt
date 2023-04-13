package aw2.g33.server.products

data class ProductDTO (
    val productId: String,
    val name: String,
    val brand: String
)

fun Product.toDTO(): ProductDTO {
    return ProductDTO(this.productId,this.name,this.brand)
}
