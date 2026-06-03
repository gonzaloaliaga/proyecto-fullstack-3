package cl.donaton.donaton.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<ErrorResponse> {
        return buildResponse(HttpStatus.NOT_FOUND, ex.message ?: "Recurso no encontrado")
    }

    private fun buildResponse(status: HttpStatus, message: String): ResponseEntity<ErrorResponseBody> {
        val body = ErrorResponseBody(
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
 */
    val status: Int,
    val error: String,
    val message: String,
    val timestamp: LocalDateTime
)