package aw2.g33.server.products


import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
class ProductController(private val productService: ProductService) {                                                                               //da json input genera json output

    @GetMapping("/API/products/")
    @ResponseStatus(HttpStatus.OK)
    fun getAll():List<ProductDTO>{
        return productService.getAll()
    }
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/API/products/{ean}")
    fun getProduct(@PathVariable ean:String):ProductDTO?{

      return productService.getProduct(ean)
    }
}
