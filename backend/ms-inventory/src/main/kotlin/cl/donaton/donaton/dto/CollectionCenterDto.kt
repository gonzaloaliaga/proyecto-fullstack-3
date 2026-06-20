package cl.donaton.donaton.dto
 
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
 
@Schema(description = "Objeto de petición para registrar un nuevo centro de acopio")
data class CreateCollectionCenterDto(
    @field:NotBlank(message = "El nombre del centro no puede estar vacío")
    @Schema(description = "Nombre del centro de acopio", example = "Centro de Acopio Central")
    val name: String,
 
    @field:NotBlank(message = "La dirección no puede estar vacía")
    @Schema(description = "Dirección física del centro", example = "Av. Libertador Bernardo O'Higgins 1234, Santiago")
    val address: String,
 
    @field:NotBlank(message = "La región no puede estar vacía")
    @Schema(description = "Región donde se ubica el centro", example = "Metropolitana")
    val region: String,
 
    @field:Min(value = 1, message = "La capacidad debe ser mayor a 0")
    @Schema(description = "Capacidad máxima de almacenamiento del centro", example = "5000")
    val capacity: Int
)
 
@Schema(description = "Objeto de petición para actualizar un centro de acopio existente")
data class UpdateCollectionCenterDto(
    @Schema(description = "Nombre del centro de acopio", example = "Centro de Acopio Central")
    val name: String? = null,
 
    @Schema(description = "Dirección física del centro", example = "Av. Libertador Bernardo O'Higgins 1234, Santiago")
    val address: String? = null,
 
    @Schema(description = "Región donde se ubica el centro", example = "Metropolitana")
    val region: String? = null,
 
    @field:Min(value = 1, message = "La capacidad debe ser mayor a 0")
    @Schema(description = "Capacidad máxima de almacenamiento del centro", example = "5000")
    val capacity: Int? = null,
 
    @Schema(description = "Indica si el centro se encuentra operativo", example = "true")
    val active: Boolean? = null
)
