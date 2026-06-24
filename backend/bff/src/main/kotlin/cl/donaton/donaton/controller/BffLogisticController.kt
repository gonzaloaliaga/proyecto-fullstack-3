package cl.donaton.donaton.controller

import cl.donaton.donaton.service.BffLogisticService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@RestController
@RequestMapping("/api")
@Tag(name = "BFF Logística", description = "Gateway para consultar necesidades y envíos")
class BffLogisticController(private val bffLogisticService: BffLogisticService) {

    @GetMapping("/needs")
    @Operation(summary = "Obtener necesidades", description = "Redirige la petición al ms-logistic para listar las necesidades")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista de necesidades obtenida exitosamente"),
            ApiResponse(responseCode = "401", description = "No autorizado, token faltante o inválido"),
            ApiResponse(responseCode = "500", description = "Error interno de comunicación")
        ]
    )
    fun getNeeds(
        @Parameter(description = "Token Bearer de autenticación", example = "Bearer eyJhbGciOiJ...")
        @RequestHeader("Authorization", required = false) authHeader: String?
    ): ResponseEntity<String> =
        bffLogisticService.getNeeds(authHeader)

    @GetMapping("/shipments")
    @Operation(summary = "Obtener envíos", description = "Redirige la petición al ms-logistic para listar los envíos")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista de envíos obtenida exitosamente"),
            ApiResponse(responseCode = "401", description = "No autorizado, token faltante o inválido"),
            ApiResponse(responseCode = "500", description = "Error interno de comunicación")
        ]
    )
    fun getShipments(
        @Parameter(description = "Token Bearer de autenticación", example = "Bearer eyJhbGciOiJ...")
        @RequestHeader("Authorization", required = false) authHeader: String?
    ): ResponseEntity<String> =
        bffLogisticService.getShipments(authHeader)
}
