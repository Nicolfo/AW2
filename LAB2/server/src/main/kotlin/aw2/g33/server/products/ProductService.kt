package aw2.g33.server.products


interface ProductService {
    fun getAll():List<ProductDTO>
    fun getProduct(productId:String): ProductDTO?

}
