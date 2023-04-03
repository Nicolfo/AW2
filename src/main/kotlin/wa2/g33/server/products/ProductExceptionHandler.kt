package wa2.g33.server.products

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ProductExceptionHandler : ResponseEntityExceptionHandler() {
        @ExceptionHandler(ProductNotFoundException::class)
        fun handleProductNotFound(e: ProductNotFoundException) = ProblemDetail
                .forStatusAndDetail( HttpStatus.NOT_FOUND, e.message!! )
}

class ProductNotFoundException(message: String?): Throwable(message){
}
