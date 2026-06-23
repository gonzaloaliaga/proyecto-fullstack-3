package cl.donaton.donaton.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Objeto de petición para el inicio de sesión")
data class LoginRequestDto(
    @Schema(
        description = "Nombre de usuario registrado", example = "juan_perez")
    val username: String,
    @Schema(
        description = "Contraseña en texto plano", example = "MiContraseñaSegura123!")
    val password: String
)