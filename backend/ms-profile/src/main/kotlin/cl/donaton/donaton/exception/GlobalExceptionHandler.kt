package cl.donaton.donaton.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(ex: UserNotFoundException): ResponseEntity<ErrorResponse> {
        val errorBody = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.reasonPhrase,
            message = ex.message ?: "Recurso no encontrado",
            timestamp = LocalDateTime.now()
        )
        return ResponseEntity(errorBody, HttpStatus.NOT_FOUND)
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