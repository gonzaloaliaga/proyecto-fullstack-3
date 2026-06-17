package cl.donaton.donaton.exception

import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals

class GlobalExceptionHandlerProfileTest {

    private val handler = GlobalExceptionHandler()

    @Test
    fun `NotFoundException debe retornar 404 con mensaje correcto`() {
        val response = handler.handleNotFoundException(NotFoundException("perfil no encontrado"))

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals("perfil no encontrado", response.body?.message)
        assertEquals(404, response.body?.status)
    }
}