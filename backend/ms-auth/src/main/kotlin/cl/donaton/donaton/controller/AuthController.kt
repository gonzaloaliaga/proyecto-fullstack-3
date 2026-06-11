package cl.donaton.donaton.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import cl.donaton.donaton.service.AuthService
import cl.donaton.donaton.factory.AuthResponse
import cl.donaton.donaton.dto.UpdateUsernameRequestDto
import cl.donaton.donaton.dto.UpdateUsernameResponseDto
import cl.donaton.donaton.dto.LoginRequestDto

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequestDto): ResponseEntity<AuthResponse> {
        val response = authService.login(loginRequest)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/update-username")
    fun updateUsername(
        @RequestHeader("Authorization") tokenHeader: String?,
        @RequestBody request: UpdateUsernameRequestDto
    ): ResponseEntity<UpdateUsernameResponseDto> {
        val response = authService.updateUsername(tokenHeader, request)
        return ResponseEntity.ok(response)
    }
}