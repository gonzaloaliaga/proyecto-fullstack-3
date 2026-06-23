package cl.donaton.donaton.controller
 
import cl.donaton.donaton.dto.AssignTransportDto
import cl.donaton.donaton.dto.CreateShipmentDto
import cl.donaton.donaton.dto.UpdateShipmentStatusDto
import cl.donaton.donaton.model.Shipment
import cl.donaton.donaton.model.ShipmentStatus
import cl.donaton.donaton.service.ShipmentService
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
@RequestMapping("/api/shipments")
@Tag(name = "Envíos", description = "Endpoints para planificar envíos, asignar transporte y hacer seguimiento hasta la entrega final")
class ShipmentController(
    private val shipmentService: ShipmentService
) {
 
    @PostMapping
    @Operation(
        summary = "Planificar un nuevo envío",
        description = "Crea un envío indicando origen, destino y fecha programada. Opcionalmente puede vincularse a una donación, cubrir una necesidad y asignar un transporte desde su creación."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Envío planificado exitosamente"),
            ApiResponse(responseCode = "400", description = "Datos del envío inválidos"),
            ApiResponse(responseCode = "404", description = "La necesidad o el transporte indicado no existe"),
            ApiResponse(responseCode = "409", description = "El transporte indicado no está disponible")
        ]
    )
    fun createShipment(@Valid @RequestBody dto: CreateShipmentDto): ResponseEntity<Shipment> {
        val created = shipmentService.createShipment(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }
 
    @GetMapping
    @Operation(
        summary = "Listar envíos",
        description = "Retorna todos los envíos, opcionalmente filtrados por estado o centro de acopio de origen."
    )
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")])
    fun getShipments(
        @Parameter(description = "Filtrar por estado del envío", example = "PLANNED")
        @RequestParam(required = false) status: ShipmentStatus?,
        @Parameter(description = "Filtrar por ID del centro de acopio de origen", example = "1")
        @RequestParam(required = false) originCenterId: Long?
    ): ResponseEntity<List<Shipment>> {
        val result = when {
            status != null -> shipmentService.getShipmentsByStatus(status)
            originCenterId != null -> shipmentService.getShipmentsByOriginCenter(originCenterId)
            else -> shipmentService.getAllShipments()
        }
        return ResponseEntity.ok(result)
    }
 
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un envío por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Envío encontrado"),
            ApiResponse(responseCode = "404", description = "El envío solicitado no existe")
        ]
    )
    fun getShipmentById(
        @Parameter(description = "ID único del envío", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Shipment> {
        return ResponseEntity.ok(shipmentService.getShipmentById(id))
    }
 
    @PatchMapping("/{id}/transport")
    @Operation(
        summary = "Asignar o reasignar transporte a un envío",
        description = "Reserva el transporte indicado (lo marca no disponible) y libera el transporte anterior si existía."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Transporte asignado exitosamente"),
            ApiResponse(responseCode = "400", description = "El envío ya fue entregado o cancelado"),
            ApiResponse(responseCode = "404", description = "El envío o el transporte indicado no existe"),
            ApiResponse(responseCode = "409", description = "El transporte indicado no está disponible")
        ]
    )
    fun assignTransport(
        @Parameter(description = "ID único del envío", example = "1")
        @PathVariable id: Long,
        @Valid @RequestBody dto: AssignTransportDto
    ): ResponseEntity<Shipment> {
        return ResponseEntity.ok(shipmentService.assignTransport(id, dto))
    }
 
    @PatchMapping("/{id}/status")
    @Operation(
        summary = "Actualizar el estado de un envío",
        description = "Avanza el ciclo de vida del envío: PLANNED -> IN_TRANSIT -> DELIVERED, o lo marca CANCELLED. " +
                "Al marcar DELIVERED se libera el transporte asignado y se cubre la necesidad asociada (si existe). " +
                "Al marcar CANCELLED se libera el transporte asignado."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            ApiResponse(responseCode = "400", description = "El envío ya fue entregado o cancelado"),
            ApiResponse(responseCode = "404", description = "El envío a actualizar no existe")
        ]
    )
    fun updateShipmentStatus(
        @Parameter(description = "ID único del envío", example = "1")
        @PathVariable id: Long,
        @Valid @RequestBody dto: UpdateShipmentStatusDto
    ): ResponseEntity<Shipment> {
        return ResponseEntity.ok(shipmentService.updateShipmentStatus(id, dto))
    }
 
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar un envío",
        description = "Elimina el registro del envío y libera el transporte asignado, si existía."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Envío eliminado exitosamente"),
            ApiResponse(responseCode = "404", description = "El envío a eliminar no existe")
        ]
    )
    fun deleteShipment(
        @Parameter(description = "ID único del envío", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        shipmentService.deleteShipment(id)
        return ResponseEntity.noContent().build()
    }
}
