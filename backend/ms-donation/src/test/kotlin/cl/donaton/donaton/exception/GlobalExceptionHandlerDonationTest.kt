package cl.donaton.donaton.exception
 
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals
 
class GlobalExceptionHandlerDonationTest {
 
    private val handler = GlobalExceptionHandler()
 
    @Test
    fun `NotFoundException debe retornar 404`() {
        val response = handler.handleNotFoundException(NotFoundException("donación no encontrada"))
 
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals("donación no encontrada", response.body?.message)
    }
 
    @Test
    fun `BadRequestException debe retornar 400`() {
        val response = handler.handleBadRequestException(BadRequestException("datos inválidos"))
 
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("datos inválidos", response.body?.message)
    }
 
    @Test
    fun `Exception generica debe retornar 500`() {
        val response = handler.handleGenericException(RuntimeException("fallo inesperado"))
 
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
    }
}
