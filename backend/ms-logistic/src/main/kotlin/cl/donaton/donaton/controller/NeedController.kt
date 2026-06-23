package cl.donaton.donaton.controller
 
import cl.donaton.donaton.dto.CreateNeedDto
import cl.donaton.donaton.dto.UpdateNeedStatusDto
import cl.donaton.donaton.model.Need
import cl.donaton.donaton.model.NeedStatus
import cl.donaton.donaton.service.NeedService
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
@RequestMapping("/api/needs")
@Tag(name = "Necesidades", description = "Endpoints para gestionar las necesidades reportadas en terreno por municipalidades, voluntarios o afectados")
class NeedController(
    private val needService: NeedService
) {
 
    @PostMapping
    @Operation(
        summary = "Reportar una nueva necesidad",
        description = "Registra un recurso requerido en una ubicación específica, indicando quién lo reporta. El estado inicial siempre es REPORTED."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Necesidad registrada exitosamente"),
            ApiResponse(responseCode = "400", description = "Datos de la necesidad inválidos")
        ]
    )
    fun createNeed(@Valid @RequestBody dto: CreateNeedDto): ResponseEntity<Need> {
        val created = needService.createNeed(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }
 
    @GetMapping
    @Operation(
        summary = "Listar necesidades",
        description = "Retorna todas las necesidades, opcionalmente filtradas por estado o ubicación."
    )
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")])
    fun getNeeds(
        @Parameter(description = "Filtrar por estado de la necesidad", example = "REPORTED")
        @RequestParam(required = false) status: NeedStatus?,
        @Parameter(description = "Filtrar por ubicación", example = "Comuna de Til Til")
        @RequestParam(required = false) location: String?
    ): ResponseEntity<List<Need>> {
        val result = when {
            status != null -> needService.getNeedsByStatus(status)
            location != null -> needService.getNeedsByLocation(location)
            else -> needService.getAllNeeds()
        }
        return ResponseEntity.ok(result)
    }
 
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una necesidad por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Necesidad encontrada"),
            ApiResponse(responseCode = "404", description = "La necesidad solicitada no existe")
        ]
    )
    fun getNeedById(
        @Parameter(description = "ID único de la necesidad", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Need> {
        return ResponseEntity.ok(needService.getNeedById(id))
    }
 
    @PatchMapping("/{id}/status")
    @Operation(
        summary = "Actualizar el estado de una necesidad",
        description = "Permite avanzar el ciclo de vida: REPORTED -> IN_PROGRESS -> COVERED, o marcarla CANCELLED."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            ApiResponse(responseCode = "404", description = "La necesidad a actualizar no existe")
        ]
    )
    fun updateNeedStatus(
        @Parameter(description = "ID único de la necesidad", example = "1")
        @PathVariable id: Long,
        @Valid @RequestBody dto: UpdateNeedStatusDto
    ): ResponseEntity<Need> {
        return ResponseEntity.ok(needService.updateNeedStatus(id, dto))
    }
 
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una necesidad")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Necesidad eliminada exitosamente"),
            ApiResponse(responseCode = "404", description = "La necesidad a eliminar no existe")
        ]
    )
    fun deleteNeed(
        @Parameter(description = "ID único de la necesidad", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        needService.deleteNeed(id)
        return ResponseEntity.noContent().build()
    }
}
