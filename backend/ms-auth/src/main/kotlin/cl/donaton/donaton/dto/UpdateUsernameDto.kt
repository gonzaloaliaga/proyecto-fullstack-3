package cl.donaton.donaton.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Objeto de petición para cambiar el nombre de usuario")
data class UpdateUsernameRequestDto(
    @Schema(
        description = "El nuevo nombre de usuario deseado", example = "nuevo_juan_2024")
    val newUsername: String
)

@Schema(description = "Objeto de respuesta tras actualizar el nombre de usuario")
data class UpdateUsernameResponseDto(
    @Schema(
        description = "Identificador único del usuario actualizado", example = "142")
    val userId: Long,
    @Schema(
        description = "El nombre de usuario que fue registrado exitosamente", example = "nuevo_juan_2024")
    val newUsername: String
)