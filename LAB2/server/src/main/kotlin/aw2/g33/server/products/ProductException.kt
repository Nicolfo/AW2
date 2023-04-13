package aw2.g33.server.products

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ProductExceptionHandler: ResponseEntityExceptionHandler() {
    @ExceptionHandler(PrimaryKeyNotFoundException::class)
    fun handlePrimaryKeyNotFount(e: PrimaryKeyNotFoundException) = ProblemDetail
        .forStatusAndDetail( HttpStatus.NOT_FOUND, e.message!! )

}

class PrimaryKeyNotFoundException(message: String?) : Throwable(message)


