package aw2.g33.server.security

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler



@RestControllerAdvice
class SecurityException: ResponseEntityExceptionHandler() {
    @ExceptionHandler(WrongCredentialsExceptions::class)
    fun handleWrongCredentialsExceptions(e: WrongCredentialsExceptions) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST,  e.message!! )

}

class WrongCredentialsExceptions(message: String?) : Throwable(message)

