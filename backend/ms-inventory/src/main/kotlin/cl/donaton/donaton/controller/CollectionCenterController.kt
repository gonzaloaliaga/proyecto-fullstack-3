package cl.donaton.donaton.controller
 
import cl.donaton.donaton.dto.CreateCollectionCenterDto
import cl.donaton.donaton.dto.UpdateCollectionCenterDto
import cl.donaton.donaton.model.CollectionCenter
import cl.donaton.donaton.service.CollectionCenterService
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
@RequestMapping("/api/collection-centers")
@Tag(name = "Centros de Acopio", description = "Endpoints para administrar las casas/centros donde se almacenan las donaciones")
class CollectionCenterController(
    private val collectionCenterService: CollectionCenterService
) {
 
    @PostMapping
    @Operation(summary = "Registrar un nuevo centro de acopio", description = "Crea un centro de acopio con su capacidad y ubicación.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Centro de acopio creado exitosamente"),
            ApiResponse(responseCode = "400", description = "Datos del centro inválidos")
        ]
    )
    fun createCollectionCenter(@Valid @RequestBody dto: CreateCollectionCenterDto): ResponseEntity<CollectionCenter> {
        val created = collectionCenterService.createCollectionCenter(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }
 
    @GetMapping
    @Operation(
        summary = "Listar centros de acopio",
        description = "Retorna todos los centros de acopio, opcionalmente filtrados por región o solo los activos."
    )
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")])
    fun getCollectionCenters(
        @Parameter(description = "Filtrar solo centros activos", example = "true")
        @RequestParam(required = false, defaultValue = "false") onlyActive: Boolean,
        @Parameter(description = "Filtrar por región", example = "Metropolitana")
        @RequestParam(required = false) region: String?
    ): ResponseEntity<List<CollectionCenter>> {
        val result = if (region != null) {
            collectionCenterService.getCollectionCentersByRegion(region)
        } else {
            collectionCenterService.getAllCollectionCenters(onlyActive)
        }
        return ResponseEntity.ok(result)
    }
 
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un centro de acopio por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Centro encontrado"),
            ApiResponse(responseCode = "404", description = "El centro solicitado no existe")
        ]
    )
    fun getCollectionCenterById(
        @Parameter(description = "ID único del centro de acopio", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<CollectionCenter> {
        return ResponseEntity.ok(collectionCenterService.getCollectionCenterById(id))
    }
 
    @PatchMapping("/{id}")
    @Operation(
        summary = "Actualizar un centro de acopio",
        description = "Actualiza los campos enviados; los campos no incluidos mantienen su valor actual."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Centro actualizado exitosamente"),
            ApiResponse(responseCode = "404", description = "El centro a actualizar no existe")
        ]
    )
    fun updateCollectionCenter(
        @Parameter(description = "ID único del centro de acopio", example = "1")
        @PathVariable id: Long,
        @RequestBody dto: UpdateCollectionCenterDto
    ): ResponseEntity<CollectionCenter> {
        return ResponseEntity.ok(collectionCenterService.updateCollectionCenter(id, dto))
    }
 
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un centro de acopio")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Centro eliminado exitosamente"),
            ApiResponse(responseCode = "404", description = "El centro a eliminar no existe")
        ]
    )
    fun deleteCollectionCenter(
        @Parameter(description = "ID único del centro de acopio", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        collectionCenterService.deleteCollectionCenter(id)
        return ResponseEntity.noContent().build()
    }
}
