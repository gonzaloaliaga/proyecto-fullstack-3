package cl.donaton.donaton.service

import cl.donaton.donaton.dto.UpdateProfileDto
import cl.donaton.donaton.exception.NotFoundException
import cl.donaton.donaton.model.Profile
import cl.donaton.donaton.repository.ProfileRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import org.springframework.data.repository.findByIdOrNull

class ProfileServiceTest {

    private val profileRepository = mockk<ProfileRepository>()
    private val profileService = ProfileService(profileRepository)

    private val perfilBase = Profile(
        id = 1L,
        role = "ADMIN",
        email = "admin@donaton.cl",
        address = "Calle Con Asfalto 123",
        run = "220330591"
    )


    @Test
    fun `getUserProfile existente debe retornar el perfil`() {
        every { profileRepository.findByIdOrNull(1L) } returns perfilBase

        val result = profileService.getUserProfile(1L)

        assertEquals(perfilBase, result)
        verify(exactly = 1) { profileRepository.findByIdOrNull(1L) }
    }

    @Test
    fun `getUserProfile inexistente debe lanzar NotFoundException`() {
        every { profileRepository.findByIdOrNull(99L) } returns null

        assertThrows<NotFoundException> {
            profileService.getUserProfile(99L)
        }
    }


    @Test
    fun `updateUserProfile debe fusionar solo los campos no nulos del DTO`() {
        val dto = UpdateProfileDto(email = "nuevo@email.cl")
        val perfilActualizado = perfilBase.copy(email = "nuevo@email.cl")

        every { profileRepository.findByIdOrNull(1L) } returns perfilBase
        every { profileRepository.save(perfilActualizado) } returns perfilActualizado

        val result = profileService.updateUserProfile(1L, dto)

        assertEquals("nuevo@email.cl", result.email)
        assertEquals("ADMIN", result.role)
        assertEquals("Calle Con Asfalto 123", result.address)
        assertEquals("220330591", result.run)
    }

    @Test
    fun `updateUserProfile con todos los campos debe reemplazarlos todos`() {
        val dto = UpdateProfileDto(
            role = "VOLUNTEER",
            email = "vol@gmail.com",
            address = "Nueva Calle 456",
            run = "111111111"
        )
        val perfilCompleto = perfilBase.copy(
            role = "VOLUNTEER", email = "vol@gmail.com",
            address = "Nueva Calle 456", run = "111111111"
        )

        every { profileRepository.findByIdOrNull(1L) } returns perfilBase
        every { profileRepository.save(perfilCompleto) } returns perfilCompleto

        val result = profileService.updateUserProfile(1L, dto)

        assertEquals("VOLUNTEER", result.role)
        assertEquals("vol@gmail.com", result.email)
    }

    @Test
    fun `updateUserProfile con DTO vacio no debe modificar ningun campo`() {
        val dto = UpdateProfileDto()
        
        every { profileRepository.findByIdOrNull(1L) } returns perfilBase
        every { profileRepository.save(perfilBase) } returns perfilBase

        val result = profileService.updateUserProfile(1L, dto)

        assertEquals(perfilBase, result)
    }

    @Test
    fun `updateUserProfile con userId inexistente debe lanzar NotFoundException`() {
        every { profileRepository.findByIdOrNull(99L) } returns null

        assertThrows<NotFoundException> {
            profileService.updateUserProfile(99L, UpdateProfileDto(role = "X"))
        }
    }

    @Test
    fun `updateUserProfile debe persistir el perfil actualizado`() {
        val dto = UpdateProfileDto(role = "DONOR")
        val perfilActualizado = perfilBase.copy(role = "DONOR")

        every { profileRepository.findByIdOrNull(1L) } returns perfilBase
        every { profileRepository.save(perfilActualizado) } returns perfilActualizado

        profileService.updateUserProfile(1L, dto)

        verify(exactly = 1) { profileRepository.save(perfilActualizado) }
    }
}