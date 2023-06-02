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
        .forStatusAndDetail(HttpStatus.UNAUTHORIZED,  e.message!! )
    @ExceptionHandler(UsernameAlreadyExistException::class)
    fun handleUsernameAlreadyExist(e: UsernameAlreadyExistException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.CONFLICT,  e.message!! )

    @ExceptionHandler(CannotFindUsernameException::class)
    fun handleCannotFindUsernameException(e: CannotFindUsernameException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.NOT_FOUND,  e.message!! )

}

class WrongCredentialsExceptions(message: String?) : Throwable(message)
class UsernameAlreadyExistException(message: String?) : Throwable(message)

class CannotFindUsernameException(message:String?): Throwable(message)


