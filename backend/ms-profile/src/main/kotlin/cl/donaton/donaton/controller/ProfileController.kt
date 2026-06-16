package cl.donaton.donaton.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

import cl.donaton.donaton.model.Profile
import cl.donaton.donaton.service.ProfileService
import cl.donaton.donaton.dto.UpdateProfileDto

@RestController
@RequestMapping("/api/profile")
@Tag(name = "Perfil", description = "Endpoints para consultar y actualizar la información de perfiles de usuario")
class ProfileController(
    private val profileService: ProfileService
) {

    @GetMapping("/{userId}")
    @Operation(
        summary = "Obtener perfil de usuario", description = "Retorna los datos del perfil asociado al ID de usuario proporcionado.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Retorna la entidad Profile encontrada"),
            ApiResponse(responseCode = "400", description = "Tipo de dato incorrecto para el parámetro userId"),
            ApiResponse(responseCode = "404", description = "No se encontró ningún perfil asociado a este ID"),
            ApiResponse(responseCode = "500", description = "Fallo de conexión con la base de datos profile-db")
        ]
    )
    fun getUserProfile(
        @Parameter(description = "ID único del usuario", example = "1")
        @PathVariable userId: Long
    ): ResponseEntity<Profile> {
    
        val profile = profileService.getUserProfile(userId)
        return ResponseEntity.ok(profile)
    }

    @PutMapping("/{userId}")
    @Operation(
        summary = "Actualizar perfil de usuario", description = "Actualiza los campos permitidos del perfil de un usuario. Solo se modificarán los campos enviados en el JSON.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
            ApiResponse(responseCode = "400", description = "Violación de constraints"),
            ApiResponse(responseCode = "403", description = "Prohibido. El token es válido, pero el ID del token no coincide con el ID del perfil que se intenta modificar."),
            ApiResponse(responseCode = "404", description = "Intento de actualizar un perfil que no existe"),
            ApiResponse(responseCode = "500", description = "Error interno del servidor")
        ]
    )
    fun updateUserProfile(
        @Parameter(description = "ID único del usuario a actualizar", example = "1")
        @PathVariable userId: Long, 
        @RequestBody updatedProfileDto: UpdateProfileDto
    ): ResponseEntity<Profile> {
        
        val updated = profileService.updateUserProfile(userId, updatedProfileDto)
        return ResponseEntity.ok(updated)
    }
}