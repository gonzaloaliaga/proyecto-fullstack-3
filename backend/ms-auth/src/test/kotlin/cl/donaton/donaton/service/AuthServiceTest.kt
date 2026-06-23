package cl.donaton.donaton.service

import cl.donaton.donaton.dto.LoginRequestDto
import cl.donaton.donaton.dto.UpdateUsernameRequestDto
import cl.donaton.donaton.exception.BadCredentialsException
import cl.donaton.donaton.exception.NotFoundException
import cl.donaton.donaton.exception.UnauthorizedException
import cl.donaton.donaton.model.User
import cl.donaton.donaton.repository.UserRepository
import cl.donaton.donaton.security.JwtService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.Optional
import kotlin.test.assertEquals

class AuthServiceTest {

    private val userRepository = mockk<UserRepository>()
    private val jwtService = mockk<JwtService>()
    private val authService = AuthService(userRepository, jwtService)


    @Test
    fun `login exitoso debe retornar AuthResponse con token`() {
        val user = User(id = 1L, username = "admin", password = "1111")
        val request = LoginRequestDto(username = "admin", password = "1111")

        every { userRepository.findByUsername("admin") } returns user
        every { jwtService.generateToken(1L, "admin") } returns "jwt-generado"

        val result = authService.login(request)

        assertEquals(1L, result.id)
        assertEquals("admin", result.username)
        assertEquals("jwt-generado", result.token)
    }

    @Test
    fun `login con usuario inexistente debe lanzar UnauthorizedException`() {
        every { userRepository.findByUsername("fantasma") } returns null

        assertThrows<UnauthorizedException> {
            authService.login(LoginRequestDto("fantasma", "1111"))
        }
    }

    @Test
    fun `login con password incorrecta debe lanzar UnauthorizedException`() {
        val user = User(id = 1L, username = "admin", password = "1111")
        every { userRepository.findByUsername("admin") } returns user

        assertThrows<UnauthorizedException> {
            authService.login(LoginRequestDto("admin", "password-erronea"))
        }
    }

    @Test
    fun `updateUsername exitoso debe actualizar y retornar el nuevo username`() {
        val user = User(id = 1L, username = "viejo", password = "1111")
        val request = UpdateUsernameRequestDto(newUsername = "nuevo")

        every { jwtService.extractUserId("token-valido") } returns 1L
        every { userRepository.findById(1L) } returns Optional.of(user)
        every { userRepository.save(any()) } returns user.copy(username = "nuevo")

        val result = authService.updateUsername("Bearer token-valido", request)

        assertEquals(1L, result.userId)
        assertEquals("nuevo", result.newUsername)
    }

    @Test
    fun `updateUsername sin header Bearer debe lanzar UnauthorizedException`() {
        assertThrows<UnauthorizedException> {
            authService.updateUsername(null, UpdateUsernameRequestDto("nuevo"))
        }
    }

    @Test
    fun `updateUsername con header sin prefijo Bearer debe lanzar UnauthorizedException`() {
        assertThrows<UnauthorizedException> {
            authService.updateUsername("token-sin-bearer", UpdateUsernameRequestDto("nuevo"))
        }
    }

    @Test
    fun `updateUsername con token invalido debe lanzar UnauthorizedException`() {
        every { jwtService.extractUserId("token-invalido") } returns null

        assertThrows<UnauthorizedException> {
            authService.updateUsername("Bearer token-invalido", UpdateUsernameRequestDto("nuevo"))
        }
    }

    @Test
    fun `updateUsername con username en blanco debe lanzar BadCredentialsException`() {
        every { jwtService.extractUserId("token-valido") } returns 1L

        assertThrows<BadCredentialsException> {
            authService.updateUsername("Bearer token-valido", UpdateUsernameRequestDto("   "))
        }
    }

    @Test
    fun `updateUsername con usuario no encontrado debe lanzar NotFoundException`() {
        every { jwtService.extractUserId("token-valido") } returns 99L
        every { userRepository.findById(99L) } returns Optional.empty()

        assertThrows<NotFoundException> {
            authService.updateUsername("Bearer token-valido", UpdateUsernameRequestDto("nuevo"))
        }
    }

    @Test
    fun `login exitoso debe llamar a generateToken exactamente una vez`() {
        val user = User(id = 1L, username = "admin", password = "1111")
        every { userRepository.findByUsername("admin") } returns user
        every { jwtService.generateToken(1L, "admin") } returns "token"

        authService.login(LoginRequestDto("admin", "1111"))

        verify(exactly = 1) { jwtService.generateToken(1L, "admin") }
    }
}