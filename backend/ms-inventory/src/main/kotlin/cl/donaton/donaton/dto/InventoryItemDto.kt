package cl.donaton.donaton.dto
 
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
 
@Schema(description = "Objeto de petición para registrar un nuevo item de inventario en un centro de acopio")
data class CreateInventoryItemDto(
    @field:NotNull(message = "El centro de acopio es obligatorio")
    @Schema(description = "ID del centro de acopio donde se almacena el recurso", example = "1")
    val collectionCenterId: Long,
 
    @field:NotBlank(message = "El recurso no puede estar vacío")
    @Schema(description = "Tipo de recurso almacenado", example = "Ropa de invierno")
    val resource: String,
 
    @field:NotNull(message = "La cantidad inicial es obligatoria")
    @Schema(description = "Cantidad inicial en stock", example = "50")
    val quantity: Int,
 
    @field:NotBlank(message = "La unidad de medida no puede estar vacía")
    @Schema(description = "Unidad de medida del recurso", example = "unidades")
    val unit: String
)
 
@Schema(description = "Objeto de petición para ajustar el stock de un item (suma o resta)")
data class AdjustStockDto(
    @field:NotNull(message = "La cantidad de ajuste es obligatoria")
    @Schema(
        description = "Cantidad a sumar (positivo) o restar (negativo) del stock actual",
        example = "-10"
    )
    val quantityChange: Int
)
