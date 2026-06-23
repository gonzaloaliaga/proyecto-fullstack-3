package cl.donaton.donaton.controller

import cl.donaton.donaton.service.BffAuthService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity
import kotlin.test.assertEquals

class BffAuthControllerTest {

    private val bffAuthService = mockk<BffAuthService>()
    private val controller = BffAuthController(bffAuthService)

    @Test
    fun `login deberia delegar al servicio y retornar su respuesta`() {
        val body = """{"username":"admin","password":"1111"}"""
        val expected = ResponseEntity.ok("""{"token":"jwt-token"}""")

        every { bffAuthService.login(body) } returns expected

        val result = controller.login(body)

        assertEquals(expected, result)
        verify(exactly = 1) { bffAuthService.login(body) }
    }

    @Test
    fun `updateUsername deberia delegar al servicio con header y body`() {
        val header = "Bearer token"
        val body = """{"newUsername":"nuevo"}"""
        val expected = ResponseEntity.ok("ok")

        every { bffAuthService.updateUsername(header, body) } returns expected

        val result = controller.updateUsername(header, null, body)

        assertEquals(expected, result)
    }
}