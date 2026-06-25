package cl.donaton.donaton.controller

import cl.donaton.donaton.service.BffDonationService
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
@RequestMapping("/api/donations")
@Tag(name = "BFF Donaciones", description = "Gateway para la gestión y consulta de donaciones")
class BffDonationController(private val bffDonationService: BffDonationService) {

    @GetMapping
    @Operation(summary = "Obtener todas las donaciones", description = "Redirige la petición al ms-donation para listar todas las donaciones")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista de donaciones obtenida exitosamente"),
            ApiResponse(responseCode = "401", description = "No autorizado, token faltante o inválido"),
            ApiResponse(responseCode = "500", description = "Error interno de comunicación")
        ]
    )
    fun getAllDonations(
        @Parameter(description = "Token Bearer de autenticación", example = "Bearer eyJhbGciOiJ...")
        @RequestHeader("Authorization", required = false) authHeader: String?
    ): ResponseEntity<String> =
        bffDonationService.getAllDonations(authHeader)

    @PostMapping
    @Operation(summary = "Crear donación", description = "Envía los datos de una nueva donación al ms-donation")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "JSON con los datos de la donación",
        content = [Content(mediaType = "application/json", schema = Schema(example = "{\n  \"userId\": 1,\n  \"amount\": 50000,\n  \"message\": \"Para los damnificados\"\n}"))]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Donación creada exitosamente"),
            ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            ApiResponse(responseCode = "401", description = "No autorizado"),
            ApiResponse(responseCode = "500", description = "Error interno del servidor")
        ]
    )
    fun createDonation(
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @RequestBody body: String
    ): ResponseEntity<String> =
        bffDonationService.createDonation(authHeader, body)

    @PatchMapping("/{id}/status")
    @Operation(summary = "Actualizar estado de donación", description = "Modifica el estado de una donación específica en el ms-donation")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "JSON con el nuevo estado",
        content = [Content(mediaType = "application/json", schema = Schema(example = "{\n  \"status\": \"COMPLETED\"\n}"))]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Estado de donación actualizado"),
            ApiResponse(responseCode = "400", description = "Estado inválido"),
            ApiResponse(responseCode = "401", description = "No autorizado"),
            ApiResponse(responseCode = "404", description = "Donación no encontrada")
        ]
    )
    fun updateDonationStatus(
        @Parameter(description = "ID único de la donación")
        @PathVariable id: Long,
        @Parameter(description = "Token Bearer de autenticación", example = "Bearer eyJhbGciOiJ...")
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @RequestBody body: String
    ): ResponseEntity<String> =
        bffDonationService.updateDonationStatus(id, authHeader, body)
}
