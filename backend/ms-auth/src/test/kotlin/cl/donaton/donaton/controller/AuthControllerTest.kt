package cl.donaton.donaton.controller

import cl.donaton.donaton.dto.LoginRequestDto
import cl.donaton.donaton.dto.UpdateUsernameRequestDto
import cl.donaton.donaton.dto.UpdateUsernameResponseDto
import cl.donaton.donaton.factory.AuthResponse
import cl.donaton.donaton.service.AuthService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals

class AuthControllerTest {

    private val authService = mockk<AuthService>()
    private val controller = AuthController(authService)

    @Test
    fun `login debe retornar 200 con AuthResponse`() {
        val request = LoginRequestDto("admin", "1111")
        val authResponse = AuthResponse(id = 1L, username = "admin", token = "jwt")

        every { authService.login(request) } returns authResponse

        val response = controller.login(request)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(authResponse, response.body)
    }

    @Test
    fun `updateUsername debe retornar 200 con UpdateUsernameResponseDto`() {
        val tokenHeader = "Bearer jwt-token"
        val request = UpdateUsernameRequestDto(newUsername = "nuevo")
        val dto = UpdateUsernameResponseDto(userId = 1L, newUsername = "nuevo")

        every { authService.updateUsername(tokenHeader, request) } returns dto

        val response = controller.updateUsername(tokenHeader, request)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("nuevo", response.body?.newUsername)
        verify(exactly = 1) { authService.updateUsername(tokenHeader, request) }
    }
}