package aw2.g33.server.profiles

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ProblemDetailsHandler: ResponseEntityExceptionHandler() {
    @ExceptionHandler(PrimaryKeyNotFoundException::class)
    fun handlePrimaryKeyNotFount(e: PrimaryKeyNotFoundException) = ProblemDetail
        .forStatusAndDetail( HttpStatus.NOT_FOUND, e.message!! )
    @ExceptionHandler(EmailConflictException::class)
    fun handleEmailConflict(e: EmailConflictException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.CONFLICT,  e.message!! )
    @ExceptionHandler(EmailInvalidException::class)
    fun handleEmailInvalid(e: EmailInvalidException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY,  e.message!! )
    @ExceptionHandler(RequestBodyException::class)
    fun handleRequestBodyError(e: RequestBodyException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST,  e.message!! )

    @ExceptionHandler(RequestParamException::class)
    fun handleRequestBodyError(e: RequestParamException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST,  e.message!! )

}

class PrimaryKeyNotFoundException(message: String?) : Throwable(message)
class EmailConflictException(message: String?) : Throwable(message)

class EmailInvalidException(message: String?) : Throwable(message)

class RequestBodyException(message: String?) : Throwable(message)
class RequestParamException(message: String?) : Throwable(message)
