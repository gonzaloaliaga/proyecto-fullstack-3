package cl.donaton.donaton.controller

import cl.donaton.donaton.service.BffAuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@RestController
@RequestMapping("/api/auth")
@Tag(name = "BFF Autenticación", description = "Gateway para el inicio de sesión y gestión de cuenta")
class BffAuthController(private val bffAuthService: BffAuthService) {

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Recibe credenciales y las redirige al ms-auth para obtener el token JWT")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "JSON con las credenciales del usuario",
        content = [Content(mediaType = "application/json", schema = Schema(example = "{\n  \"username\": \"mi_usuario\",\n  \"password\": \"mi_clave123\"\n}"))]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Login exitoso, retorna el JWT y datos del usuario"),
            ApiResponse(responseCode = "400", description = "Petición mal formada"),
            ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            ApiResponse(responseCode = "500", description = "Error interno de conexión")
        ]
    )
    fun login(@RequestBody body: String): ResponseEntity<String> {
        return bffAuthService.login(body)
    }

    @PostMapping("/update-username")
    @Operation(summary = "Actualizar nombre de usuario", description = "Redirige la solicitud de cambio de nombre de usuario al ms-auth")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "JSON con el nuevo nombre de usuario",
        content = [Content(mediaType = "application/json", schema = Schema(example = "{\n  \"newUsername\": \"nuevo_nombre2024\"\n}"))]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Nombre de usuario actualizado exitosamente"),
            ApiResponse(responseCode = "400", description = "El nuevo username ya está en uso o el formato es inválido"),
            ApiResponse(responseCode = "401", description = "Token JWT faltante, expirado o inválido"),
            ApiResponse(responseCode = "404", description = "El usuario no existe en la base de datos"),
            ApiResponse(responseCode = "500", description = "Error interno en el servidor")
        ]
    )
    fun updateUsername(
        @Parameter(description = "Token Bearer de autenticación", example = "Bearer eyJhbGciOiJ...")
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @Parameter(hidden = true)
        @RequestAttribute("authenticatedUserId", required = false) authenticatedUserId: String?,
        @RequestBody requestBody: String
    ): ResponseEntity<String> {
        return bffAuthService.updateUsername(authHeader, requestBody)
    }
}