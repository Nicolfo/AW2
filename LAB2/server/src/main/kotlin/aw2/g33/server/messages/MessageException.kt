package aw2.g33.server.messages

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class MessageException : ResponseEntityExceptionHandler(){

    @ExceptionHandler(TicketStatusError::class)
    fun handleServiceWithNullParams(e:TicketStatusError) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST,  e.message!! )
    @ExceptionHandler(MessageBodyException::class)
    fun handleMessageBodyException(e:MessageBodyException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST,  e.message!! )
    @ExceptionHandler(MessageParamException::class)
    fun handleMessageBodyException(e:MessageParamException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST,  e.message!! )
    @ExceptionHandler(EmptyChatException::class)
    fun handleEmptyChatException(e:EmptyChatException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST,  e.message!! )

    @ExceptionHandler(TicketIDNotFoundException::class)
    fun handleEmptyChatException(e:TicketIDNotFoundException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.NOT_FOUND,  e.message!! )


}
class TicketStatusError(message: String?) : Throwable(message)
class MessageParamException(message:String?):Throwable(message)
class MessageBodyException(message: String?) : Throwable(message)
class EmptyChatException(message: String?) : Throwable(message)
class TicketIDNotFoundException(message: String?) : Throwable(message)