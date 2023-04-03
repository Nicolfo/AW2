package wa2.g33.server.products

data class ProductDTO (
        val productId: String,
        val name: String,
        val brand: String
)

fun Product.toDTO(): ProductDTO{
    return ProductDTO(productid,name,brand)
}
