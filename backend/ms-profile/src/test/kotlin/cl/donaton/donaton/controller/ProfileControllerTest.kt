package cl.donaton.donaton.controller

import cl.donaton.donaton.dto.UpdateProfileDto
import cl.donaton.donaton.model.Profile
import cl.donaton.donaton.service.ProfileService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals

class ProfileControllerTest {

    private val profileService = mockk<ProfileService>()
    private val controller = ProfileController(profileService)

    private val perfil = Profile(1L, "ADMIN", "admin@donaton.cl", "Calle 123", "220330591")

    @Test
    fun `getUserProfile debe retornar 200 con el perfil`() {
        every { profileService.getUserProfile(1L) } returns perfil

        val response = controller.getUserProfile(1L)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(perfil, response.body)
        verify(exactly = 1) { profileService.getUserProfile(1L) }
    }

    @Test
    fun `updateUserProfile debe retornar 200 con perfil actualizado`() {
        val dto = UpdateProfileDto(role = "VOLUNTEER")
        val actualizado = perfil.copy(role = "VOLUNTEER")

        every { profileService.updateUserProfile(1L, dto) } returns actualizado

        val response = controller.updateUserProfile(1L, dto)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("VOLUNTEER", response.body?.role)
    }
}