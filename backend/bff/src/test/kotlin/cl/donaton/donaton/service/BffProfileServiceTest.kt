package cl.donaton.donaton.service

import cl.donaton.donaton.client.ProfileClient
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity
import kotlin.test.assertEquals

class BffProfileServiceTest {

    private val profileClient = mockk<ProfileClient>()
    private val bffProfileService = BffProfileService(profileClient)

    @Test
    fun `getUserProfile deberia derivar al profileClient y retornar su respuesta`() {
        val userId = 1L
        val authHeader = "Bearer token-valido"
        val respuestaEsperada = ResponseEntity.ok("""{"id":1,"role":"ADMIN"}""")

        every { profileClient.fetchUserProfile(userId, authHeader) } returns respuestaEsperada

        val resultado = bffProfileService.getUserProfile(userId, authHeader)

        assertEquals(respuestaEsperada, resultado)
        verify(exactly = 1) { profileClient.fetchUserProfile(userId, authHeader) }
    }

    @Test
    fun `updateUserProfile deberia derivar al profileClient y retornar su respuesta`() {
        val userId = 1L
        val authHeader = "Bearer token-valido"
        val body = """{"role":"VOLUNTEER"}"""
        val respuestaEsperada = ResponseEntity.ok("""{"id":1,"role":"VOLUNTEER"}""")

        every { profileClient.forwardUpdateProfile(userId, authHeader, body) } returns respuestaEsperada

        val resultado = bffProfileService.updateUserProfile(userId, authHeader, body)

        assertEquals(respuestaEsperada, resultado)
        verify(exactly = 1) { profileClient.forwardUpdateProfile(userId, authHeader, body) }
    }

    @Test
    fun `getUserProfile con authHeader nulo deberia igualmente delegar al client`() {
        val userId = 2L
        val respuestaEsperada = ResponseEntity.ok("""{"id":2}""")

        every { profileClient.fetchUserProfile(userId, null) } returns respuestaEsperada

        val resultado = bffProfileService.getUserProfile(userId, null)

        assertEquals(respuestaEsperada, resultado)
    }
}