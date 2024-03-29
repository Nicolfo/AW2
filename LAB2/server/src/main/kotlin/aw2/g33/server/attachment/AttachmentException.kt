package aw2.g33.server.attachment

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class AttachmentException : ResponseEntityExceptionHandler(){


    @ExceptionHandler(MessageNotFoundException::class)
    fun handleMessageNotFoundException(e:MessageNotFoundException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST,  e.message!! )
    @ExceptionHandler(AttachmentOutOfBoundException::class)
    fun handleAttachmentOutOfBoundException(e:AttachmentOutOfBoundException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST,  e.message!! )

}

class MessageNotFoundException(message: String?) : Throwable(message)
class AttachmentOutOfBoundException(message: String?) : Throwable(message)