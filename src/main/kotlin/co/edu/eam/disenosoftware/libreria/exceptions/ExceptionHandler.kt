package co.edu.eam.disenosoftware.libreria.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import javax.persistence.EntityNotFoundException

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(BusinessException::class) //excepcion que se quiere atrapar
    @ResponseBody //que la respuesta va a ser personalizada
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    fun handleBusinessException(exc: BusinessException): ErrorResponse {
        return ErrorResponse(message = exc.message?:"BusinessError", 412)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class) //excepcion que se quiere atrapar
    @ResponseBody //que la respuesta va a ser personalizada
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValid(exc: MethodArgumentNotValidException): ErrorResponse {
        return ErrorResponse(message = exc.message?:"Bad request", 400)
    }

    @ExceptionHandler(EntityNotFoundException::class) //excepcion que se quiere atrapar
    @ResponseBody //que la respuesta va a ser personalizada
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleEntityNotFoundException(exc: EntityNotFoundException): ErrorResponse {
        return ErrorResponse(message = exc.message?:"Not found", 404)
    }
}