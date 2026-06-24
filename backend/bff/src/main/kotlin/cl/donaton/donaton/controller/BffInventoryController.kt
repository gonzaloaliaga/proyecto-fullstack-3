package cl.donaton.donaton.controller

import cl.donaton.donaton.service.BffInventoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@RestController
@RequestMapping("/api")
@Tag(name = "BFF Inventario", description = "Gateway para consultar inventarios y centros de acopio")
class BffInventoryController(private val bffInventoryService: BffInventoryService) {

    @GetMapping("/collection-centers")
    @Operation(summary = "Obtener centros de acopio", description = "Redirige la petición al ms-inventory para listar los centros de acopio")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista de centros de acopio obtenida exitosamente"),
            ApiResponse(responseCode = "401", description = "No autorizado, token faltante o inválido"),
            ApiResponse(responseCode = "500", description = "Error interno de comunicación")
        ]
    )
    fun getCollectionCenters(
        @Parameter(description = "Token Bearer de autenticación", example = "Bearer eyJhbGciOiJ...")
        @RequestHeader("Authorization", required = false) authHeader: String?
    ): ResponseEntity<String> =
        bffInventoryService.getCollectionCenters(authHeader)

    @GetMapping("/inventory-items")
    @Operation(summary = "Obtener ítems de inventario", description = "Redirige la petición al ms-inventory para listar los ítems de inventario")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista de ítems de inventario obtenida exitosamente"),
            ApiResponse(responseCode = "401", description = "No autorizado, token faltante o inválido"),
            ApiResponse(responseCode = "500", description = "Error interno de comunicación")
        ]
    )
    fun getInventoryItems(
        @Parameter(description = "Token Bearer de autenticación", example = "Bearer eyJhbGciOiJ...")
        @RequestHeader("Authorization", required = false) authHeader: String?
    ): ResponseEntity<String> =
        bffInventoryService.getInventoryItems(authHeader)
}
