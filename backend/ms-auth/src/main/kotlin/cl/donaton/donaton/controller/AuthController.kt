package cl.donaton.donaton.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import cl.donaton.donaton.service.AuthService
import cl.donaton.donaton.factory.AuthResponse
import cl.donaton.donaton.dto.UpdateUsernameRequestDto
import cl.donaton.donaton.dto.UpdateUsernameResponseDto
import cl.donaton.donaton.dto.LoginRequestDto

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para el manejo de sesiones y usuarios")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Valida las credenciales y devuelve un token JWT")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Credenciales correctas, retorna token"),
            ApiResponse(responseCode = "400", description = "Faltan campos obligatorios"),
            ApiResponse(responseCode = "401", description = "Usuario o contraseña incorrectos"),
            ApiResponse(responseCode = "404", description = "Usuario no registrado en el sistema"),
            ApiResponse(responseCode = "500", description = "Error interno de base de datos")
        ]
    )
    fun login(@RequestBody loginRequest: LoginRequestDto): ResponseEntity<AuthResponse> {
        val response = authService.login(loginRequest)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/update-username")
    @Operation(
        summary = "Actualizar nombre de usuario", description = "Permite a un usuario autenticado cambiar su nombre de usuario actual por uno nuevo.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Username cambiado con éxito"),
            ApiResponse(responseCode = "400", description = "El nuevo username está en uso"),
            ApiResponse(responseCode = "401", description = "El token está corrupto, expirado o manipulado"),
            ApiResponse(responseCode = "404", description = "El ID extraído del token no existe en la tabla de usuarios"),
            ApiResponse(responseCode = "500", description = "Error al intentar guardar en la base de datos")
        ]
    )
    fun updateUsername(
        @Parameter(description = "Token extraído por el BFF", required = true)
        @RequestHeader("Authorization") tokenHeader: String?,
        @RequestBody request: UpdateUsernameRequestDto
    ): ResponseEntity<UpdateUsernameResponseDto> {
        val response = authService.updateUsername(tokenHeader, request)
        return ResponseEntity.ok(response)
    }
}