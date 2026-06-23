package cl.donaton.donaton.exception
 
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals
 
class GlobalExceptionHandlerInventoryTest {
 
    private val handler = GlobalExceptionHandler()
 
    @Test
    fun `NotFoundException debe retornar 404`() {
        val response = handler.handleNotFoundException(NotFoundException("centro no encontrado"))
 
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }
 
    @Test
    fun `BadRequestException debe retornar 400`() {
        val response = handler.handleBadRequestException(BadRequestException("solicitud invalida"))
 
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }
 
    @Test
    fun `InsufficientStockException debe retornar 409`() {
        val response = handler.handleInsufficientStockException(InsufficientStockException("stock insuficiente"))
 
        assertEquals(HttpStatus.CONFLICT, response.statusCode)
        assertEquals("stock insuficiente", response.body?.message)
    }
 
    @Test
    fun `Exception generica debe retornar 500`() {
        val response = handler.handleGenericException(RuntimeException("fallo inesperado"))
 
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
    }
}
