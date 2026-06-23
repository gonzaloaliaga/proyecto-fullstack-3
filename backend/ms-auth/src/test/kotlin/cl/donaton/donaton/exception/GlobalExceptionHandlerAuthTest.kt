package cl.donaton.donaton.exception

import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals

class GlobalExceptionHandlerAuthTest {

    private val handler = GlobalExceptionHandler()

    @Test
    fun `UnauthorizedException debe retornar 403`() {
        val response = handler.handleUnauthorizedException(UnauthorizedException("sin acceso"))
        assertEquals(HttpStatus.FORBIDDEN, response.statusCode)
        assertEquals("sin acceso", response.body?.message)
    }

    @Test
    fun `NotFoundException debe retornar 404`() {
        val response = handler.handleNotFoundException(NotFoundException("no existe"))
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `BadCredentialsException debe retornar 401`() {
        val response = handler.handleBadCredentialsException(BadCredentialsException("credenciales malas"))
        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
    }

    @Test
    fun `Exception generica debe retornar 500`() {
        val response = handler.handleGenericException(RuntimeException("fallo raro"))
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
    }
}