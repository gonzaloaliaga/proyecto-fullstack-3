package cl.donaton.donaton.controller
 
import cl.donaton.donaton.dto.CreateDonationDto
import cl.donaton.donaton.dto.UpdateDonationStatusDto
import cl.donaton.donaton.model.Donation
import cl.donaton.donaton.model.DonationStatus
import cl.donaton.donaton.service.DonationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
 
@RestController
@RequestMapping("/api/donations")
@Tag(name = "Donaciones", description = "Endpoints para el registro y seguimiento de donaciones recibidas")
class DonationController(
    private val donationService: DonationService
) {
 
    @PostMapping
    @Operation(
        summary = "Registrar una nueva donación",
        description = "Crea una donación detallando recurso, cantidad, origen, fecha y centro de acopio asignado. El estado inicial siempre es PENDING."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Donación registrada exitosamente"),
            ApiResponse(responseCode = "400", description = "Datos de la donación inválidos"),
            ApiResponse(responseCode = "500", description = "Error interno del servidor")
        ]
    )
    fun createDonation(@Valid @RequestBody dto: CreateDonationDto): ResponseEntity<Donation> {
        val created = donationService.createDonation(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }
 
    @GetMapping
    @Operation(
        summary = "Listar donaciones",
        description = "Retorna todas las donaciones, opcionalmente filtradas por centro de acopio o estado."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listado de donaciones obtenido exitosamente")
        ]
    )
    fun getDonations(
        @Parameter(description = "Filtrar por ID del centro de acopio", example = "1")
        @RequestParam(required = false) collectionCenterId: Long?,
        @Parameter(description = "Filtrar por estado de la donación", example = "PENDING")
        @RequestParam(required = false) status: DonationStatus?
    ): ResponseEntity<List<Donation>> {
        val result = when {
            collectionCenterId != null -> donationService.getDonationsByCollectionCenter(collectionCenterId)
            status != null -> donationService.getDonationsByStatus(status)
            else -> donationService.getAllDonations()
        }
        return ResponseEntity.ok(result)
    }
 
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una donación por ID", description = "Retorna el detalle de una donación específica.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Donación encontrada"),
            ApiResponse(responseCode = "404", description = "La donación solicitada no existe")
        ]
    )
    fun getDonationById(
        @Parameter(description = "ID único de la donación", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Donation> {
        return ResponseEntity.ok(donationService.getDonationById(id))
    }
 
    @PatchMapping("/{id}/status")
    @Operation(
        summary = "Actualizar el estado de una donación",
        description = "Permite avanzar el ciclo de vida de la donación: PENDING -> RECEIVED -> ASSIGNED -> DELIVERED."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            ApiResponse(responseCode = "400", description = "Estado inválido"),
            ApiResponse(responseCode = "404", description = "La donación a actualizar no existe")
        ]
    )
    fun updateDonationStatus(
        @Parameter(description = "ID único de la donación", example = "1")
        @PathVariable id: Long,
        @Valid @RequestBody dto: UpdateDonationStatusDto
    ): ResponseEntity<Donation> {
        return ResponseEntity.ok(donationService.updateDonationStatus(id, dto))
    }
 
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una donación", description = "Elimina el registro de una donación del sistema.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Donación eliminada exitosamente"),
            ApiResponse(responseCode = "404", description = "La donación a eliminar no existe")
        ]
    )
    fun deleteDonation(
        @Parameter(description = "ID único de la donación", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        donationService.deleteDonation(id)
        return ResponseEntity.noContent().build()
    }
}
