package cl.donaton.donaton.controller
 
import cl.donaton.donaton.dto.AdjustStockDto
import cl.donaton.donaton.dto.CreateInventoryItemDto
import cl.donaton.donaton.model.InventoryItem
import cl.donaton.donaton.service.InventoryItemService
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
@RequestMapping("/api/inventory-items")
@Tag(name = "Inventario", description = "Endpoints para controlar el stock de recursos por centro de acopio")
class InventoryItemController(
    private val inventoryItemService: InventoryItemService
) {
 
    @PostMapping
    @Operation(
        summary = "Registrar un nuevo item de inventario",
        description = "Crea un registro de stock para un recurso dentro de un centro de acopio existente."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Item de inventario creado exitosamente"),
            ApiResponse(responseCode = "400", description = "Datos del item inválidos"),
            ApiResponse(responseCode = "404", description = "El centro de acopio indicado no existe")
        ]
    )
    fun createInventoryItem(@Valid @RequestBody dto: CreateInventoryItemDto): ResponseEntity<InventoryItem> {
        val created = inventoryItemService.createInventoryItem(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }
 
    @GetMapping
    @Operation(
        summary = "Listar items de inventario",
        description = "Retorna todos los items, opcionalmente filtrados por centro de acopio."
    )
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")])
    fun getInventoryItems(
        @Parameter(description = "Filtrar por ID del centro de acopio", example = "1")
        @RequestParam(required = false) collectionCenterId: Long?
    ): ResponseEntity<List<InventoryItem>> {
        val result = if (collectionCenterId != null) {
            inventoryItemService.getInventoryItemsByCollectionCenter(collectionCenterId)
        } else {
            inventoryItemService.getAllInventoryItems()
        }
        return ResponseEntity.ok(result)
    }
 
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un item de inventario por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Item encontrado"),
            ApiResponse(responseCode = "404", description = "El item solicitado no existe")
        ]
    )
    fun getInventoryItemById(
        @Parameter(description = "ID único del item de inventario", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<InventoryItem> {
        return ResponseEntity.ok(inventoryItemService.getInventoryItemById(id))
    }
 
    @PatchMapping("/{id}/stock")
    @Operation(
        summary = "Ajustar el stock de un item",
        description = "Suma o resta una cantidad al stock actual. Rechaza la operación si el resultado sería negativo."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Stock ajustado exitosamente"),
            ApiResponse(responseCode = "404", description = "El item a ajustar no existe"),
            ApiResponse(responseCode = "409", description = "Stock insuficiente para realizar el ajuste")
        ]
    )
    fun adjustStock(
        @Parameter(description = "ID único del item de inventario", example = "1")
        @PathVariable id: Long,
        @Valid @RequestBody dto: AdjustStockDto
    ): ResponseEntity<InventoryItem> {
        return ResponseEntity.ok(inventoryItemService.adjustStock(id, dto))
    }
 
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un item de inventario")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Item eliminado exitosamente"),
            ApiResponse(responseCode = "404", description = "El item a eliminar no existe")
        ]
    )
    fun deleteInventoryItem(
        @Parameter(description = "ID único del item de inventario", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        inventoryItemService.deleteInventoryItem(id)
        return ResponseEntity.noContent().build()
    }
}
