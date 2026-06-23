package cl.donaton.donaton.controller

import cl.donaton.donaton.service.BffProfileService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity
import kotlin.test.assertEquals

class BffProfileControllerTest {

    private val bffProfileService = mockk<BffProfileService>()
    private val controller = BffProfileController(bffProfileService)

    @Test
    fun `getUserProfile debe delegar al servicio y retornar su respuesta`() {
        val userId = 1L
        val authHeader = "Bearer token-valido"
        val expected = ResponseEntity.ok("""{"id":1,"role":"ADMIN"}""")

        every { bffProfileService.getUserProfile(userId, authHeader) } returns expected

        val result = controller.getUserProfile(userId, authHeader, null)

        assertEquals(expected, result)
        verify(exactly = 1) { bffProfileService.getUserProfile(userId, authHeader) }
    }

    @Test
    fun `getUserProfile con authHeader nulo debe igualmente delegar al servicio`() {
        val userId = 2L
        val expected = ResponseEntity.ok("""{"id":2,"role":"DONOR"}""")

        every { bffProfileService.getUserProfile(userId, null) } returns expected

        val result = controller.getUserProfile(userId, null, null)

        assertEquals(expected, result)
        verify(exactly = 1) { bffProfileService.getUserProfile(userId, null) }
    }

    @Test
    fun `updateUserProfile debe delegar al servicio con userId authHeader y body`() {
        val userId = 1L
        val authHeader = "Bearer token-valido"
        val body = """{"role":"VOLUNTEER","email":"vol@gmail.com"}"""
        val expected = ResponseEntity.ok("""{"id":1,"role":"VOLUNTEER"}""")

        every { bffProfileService.updateUserProfile(userId, authHeader, body) } returns expected

        val result = controller.updateUserProfile(userId, authHeader, null, body)

        assertEquals(expected, result)
        verify(exactly = 1) { bffProfileService.updateUserProfile(userId, authHeader, body) }
    }

    @Test
    fun `updateUserProfile con authHeader nulo debe igualmente delegar al servicio`() {
        val userId = 1L
        val body = """{"role":"DONOR"}"""
        val expected = ResponseEntity.ok("""{"id":1,"role":"DONOR"}""")

        every { bffProfileService.updateUserProfile(userId, null, body) } returns expected

        val result = controller.updateUserProfile(userId, null, null, body)

        assertEquals(expected, result)
    }
}