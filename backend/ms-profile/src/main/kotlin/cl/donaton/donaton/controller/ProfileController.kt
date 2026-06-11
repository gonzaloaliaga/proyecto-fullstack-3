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
            ApiResponse(responseCode = "200", description = "Perfil encontrado exitosamente"),
            ApiResponse(responseCode = "404", description = "Perfil no encontrado")
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
            ApiResponse(responseCode = "400", description = "Datos proporcionados inválidos"),
            ApiResponse(responseCode = "404", description = "Perfil no encontrado")
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