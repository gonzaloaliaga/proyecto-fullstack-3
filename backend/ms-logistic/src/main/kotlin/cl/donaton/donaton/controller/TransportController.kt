package cl.donaton.donaton.controller
 
import cl.donaton.donaton.dto.CreateTransportDto
import cl.donaton.donaton.model.Transport
import cl.donaton.donaton.service.TransportService
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
@RequestMapping("/api/transports")
@Tag(name = "Transportes", description = "Endpoints para administrar los vehículos y conductores disponibles para realizar envíos")
class TransportController(
    private val transportService: TransportService
) {
 
    @PostMapping
    @Operation(summary = "Registrar un nuevo transporte", description = "Crea un transporte disponible para asignar a futuros envíos.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Transporte creado exitosamente"),
            ApiResponse(responseCode = "400", description = "Datos del transporte inválidos")
        ]
    )
    fun createTransport(@Valid @RequestBody dto: CreateTransportDto): ResponseEntity<Transport> {
        val created = transportService.createTransport(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }
 
    @GetMapping
    @Operation(
        summary = "Listar transportes",
        description = "Retorna todos los transportes, opcionalmente filtrados para mostrar solo los disponibles."
    )
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")])
    fun getTransports(
        @Parameter(description = "Filtrar solo transportes disponibles", example = "true")
        @RequestParam(required = false, defaultValue = "false") onlyAvailable: Boolean
    ): ResponseEntity<List<Transport>> {
        return ResponseEntity.ok(transportService.getAllTransports(onlyAvailable))
    }
 
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un transporte por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Transporte encontrado"),
            ApiResponse(responseCode = "404", description = "El transporte solicitado no existe")
        ]
    )
    fun getTransportById(
        @Parameter(description = "ID único del transporte", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Transport> {
        return ResponseEntity.ok(transportService.getTransportById(id))
    }
 
    @PatchMapping("/{id}/availability")
    @Operation(
        summary = "Cambiar la disponibilidad de un transporte",
        description = "Permite marcar manualmente un transporte como disponible u ocupado, por ejemplo por mantención."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Disponibilidad actualizada exitosamente"),
            ApiResponse(responseCode = "404", description = "El transporte a actualizar no existe")
        ]
    )
    fun setTransportAvailability(
        @Parameter(description = "ID único del transporte", example = "1")
        @PathVariable id: Long,
        @Parameter(description = "Nueva disponibilidad", example = "false")
        @RequestParam available: Boolean
    ): ResponseEntity<Transport> {
        return ResponseEntity.ok(transportService.setTransportAvailability(id, available))
    }
 
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un transporte")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Transporte eliminado exitosamente"),
            ApiResponse(responseCode = "404", description = "El transporte a eliminar no existe")
        ]
    )
    fun deleteTransport(
        @Parameter(description = "ID único del transporte", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        transportService.deleteTransport(id)
        return ResponseEntity.noContent().build()
    }
}
