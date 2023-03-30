package aw2.g33.server.products

data class ProductDTO (
    val ean: String,
    val name: String,
    val brand: String
)

fun Product.toDTO(): ProductDTO {
    return ProductDTO(this.ean,this.name,this.brand)
}
