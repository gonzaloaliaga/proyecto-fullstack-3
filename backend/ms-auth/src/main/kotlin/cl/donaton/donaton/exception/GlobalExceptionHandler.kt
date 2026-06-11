package cl.donaton.donaton.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

import cl.donaton.donaton.exception.UnauthorizedException
import cl.donaton.donaton.exception.NotFoundException
import cl.donaton.donaton.exception.BadCredentialsException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(ex: UnauthorizedException): ResponseEntity<ErrorResponse> {
        return  buildResponse(HttpStatus.FORBIDDEN, ex.message ?: "Acceso no autorizado")
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<ErrorResponse> {
        return  buildResponse(HttpStatus.NOT_FOUND, ex.message ?: "Recurso no encontrado")
    }
    
    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(ex: BadCredentialsException): ResponseEntity<ErrorResponse> {
        return  buildResponse(HttpStatus.UNAUTHORIZED, ex.message ?: "Datos de solicitud inválidos")
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error interno en el servidor")
    }

    private fun buildResponse(status: HttpStatus, message: String): ResponseEntity<ErrorResponse> {
        val body = ErrorResponse(
            status = status.value(),
            error = status.reasonPhrase,
            message = message,
            timestamp = LocalDateTime.now()
        )
        return ResponseEntity(body, status)
    }
}

/* DTO para dar formato al JSON al error */
data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String,
    val timestamp: LocalDateTime
)