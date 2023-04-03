package aw2.g33.server.products

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(private val productRepository: ProductRepository): ProductService {

    override fun getAll(): List<ProductDTO> {

        return productRepository.findAll().map {it.toDTO()}
    }

    override fun getProduct(ean: String): ProductDTO? {
        val product=productRepository.findByIdOrNull(ean)
        if(product==null) throw PrimaryKeyNotFoundException("ean not found");

        return product?.toDTO()
    }

}
