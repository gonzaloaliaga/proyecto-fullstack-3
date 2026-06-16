package cl.donaton.donaton.controller

import cl.donaton.donaton.service.BffProfileService
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
@RequestMapping("/api/profile")
@Tag(name = "BFF Perfil", description = "Gateway para consultar y editar la información de los perfiles")
class BffProfileController(private val bffProfileService: BffProfileService) {

    @GetMapping("/{userId}")
    @Operation(summary = "Obtener perfil", description = "Solicita los datos del perfil al ms-profile mediante el ID del usuario")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Perfil recuperado exitosamente"),
            ApiResponse(responseCode = "400", description = "El ID del usuario tiene un formato inválido"),
            ApiResponse(responseCode = "401", description = "No autorizado, token inválido"),
            ApiResponse(responseCode = "403", description = "Prohibido, no tienes permisos para ver este perfil"),
            ApiResponse(responseCode = "404", description = "El perfil solicitado no existe"),
            ApiResponse(responseCode = "500", description = "Error de comunicación")
        ]
    )
    fun getUserProfile(
        @Parameter(description = "ID único del usuario", example = "1")
        @PathVariable userId: Long,
        @Parameter(description = "Token Bearer de autenticación", example = "Bearer eyJhbGciOiJ...")
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @Parameter(hidden = true)
        @RequestAttribute("authenticatedUserId", required = false) authenticatedUserId: String?
    ): ResponseEntity<String> {

        return bffProfileService.getUserProfile(userId, authHeader)
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Actualizar perfil", description = "Redirige los nuevos datos del perfil al ms-profile para su actualización")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "JSON con los datos del perfil a modificar",
        content = [Content(mediaType = "application/json", schema = Schema(example = "{\n  \"firstName\": \"Juan\",\n  \"lastName\": \"Pérez\",\n  \"phone\": \"+56912345678\"\n}"))]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
            ApiResponse(responseCode = "400", description = "Datos inválidos en el cuerpo de la petición"),
            ApiResponse(responseCode = "401", description = "Token JWT no proporcionado o inválido"),
            ApiResponse(responseCode = "403", description = "Prohibido, el token no pertenece al usuario que se intenta modificar"),
            ApiResponse(responseCode = "404", description = "El perfil a modificar no existe"),
            ApiResponse(responseCode = "500", description = "Error interno del servidor")
        ]
    )
    fun updateUserProfile(
        @Parameter(description = "ID único del usuario a editar", example = "1")
        @PathVariable userId: Long,
        @Parameter(description = "Token Bearer de autenticación", example = "Bearer eyJhbGciOiJ...")
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @Parameter(hidden = true)
        @RequestAttribute("authenticatedUserId", required = false) authenticatedUserId: String?,
        @RequestBody updatedProfile: String
    ): ResponseEntity<String> {
        
        return bffProfileService.updateUserProfile(userId, authHeader, updatedProfile)
    }
}