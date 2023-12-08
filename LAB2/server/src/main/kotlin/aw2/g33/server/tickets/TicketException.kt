package aw2.g33.server.tickets



import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
@CrossOrigin
class TicketException : ResponseEntityExceptionHandler(){
    @ExceptionHandler(PrimaryKeyNotFoundException::class)
    fun handlePrimaryKeyNotFount(e: PrimaryKeyNotFoundException) = ProblemDetail
        .forStatusAndDetail( HttpStatus.NOT_FOUND, e.message!! )

    @ExceptionHandler(RequestBodyException::class)
    fun handleRequestBodyError(e: RequestBodyException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST,  e.message!! )

    @ExceptionHandler(StatusTransitionIncorrect::class)
    fun handleStatusTransitionError(e: StatusTransitionIncorrect) = ProblemDetail
        .forStatusAndDetail(HttpStatus.CONFLICT,  e.message!! )

    @ExceptionHandler(RequestParamException::class)
    fun handleRequestBodyError(e: RequestParamException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST,  e.message!! )
    @ExceptionHandler(ServiceWithNullParams::class)
    fun handleServiceWithNullParams(e:ServiceWithNullParams) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST,  e.message!! )
    @ExceptionHandler(CredentialNotMatching::class)
    fun handleCredentialNotMatching(e:CredentialNotMatching) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST,  e.message!! )
    @ExceptionHandler(InvalidPermsssionUser::class)
    fun handleInvalidPermssionUser(e:InvalidPermsssionUser) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST,  e.message!! )

}
class PrimaryKeyNotFoundException(message: String?) : Throwable(message)
class StatusTransitionIncorrect(message: String?) : Throwable(message)
class RequestBodyException(message: String?) : Throwable(message)
class RequestParamException(message: String?) : Throwable(message)
class ServiceWithNullParams(message: String?):Throwable(message)
class CredentialNotMatching(message: String?):Throwable(message)
class InvalidPermsssionUser(message: String?):Throwable(message)
