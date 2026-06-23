package cl.donaton.donaton.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Objeto de petición para actualizar el perfil. Todos los campos son opcionales, solo se actualizarán los que se envíen.")
data class UpdateProfileDto(
    @Schema(description = "Rol del usuario en el sistema", example = "DONANTE")
    val role: String? = null,
    @Schema(description = "Correo electrónico", example = "usuario@correo.com")
    val email: String? = null,
    @Schema(description = "Dirección de residencia o despacho", example = "Av. Siempre Viva 742, Comuna")
    val address: String? = null,
    @Schema(description = "RUN sin puntos y con guión", example = "12345678-9")
    val run: String? = null
)