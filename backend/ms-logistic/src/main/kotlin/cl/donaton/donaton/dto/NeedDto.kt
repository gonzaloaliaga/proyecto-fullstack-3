package cl.donaton.donaton.dto
 
import cl.donaton.donaton.model.NeedStatus
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
 
@Schema(description = "Objeto de petición para reportar una necesidad en terreno")
data class CreateNeedDto(
    @field:NotBlank(message = "El recurso necesitado no puede estar vacío")
    @Schema(description = "Tipo de recurso requerido", example = "Agua potable")
    val resource: String,
 
    @field:Min(value = 1, message = "La cantidad debe ser mayor a 0")
    @Schema(description = "Cantidad requerida", example = "500")
    val quantity: Int,
 
    @field:NotBlank(message = "La ubicación no puede estar vacía")
    @Schema(description = "Ubicación geográfica de la necesidad", example = "Comuna de Til Til")
    val location: String,
 
    @Schema(description = "Latitud de la ubicación (opcional)", example = "-33.0833")
    val latitude: Double? = null,
 
    @Schema(description = "Longitud de la ubicación (opcional)", example = "-70.9333")
    val longitude: Double? = null,
 
    @field:NotBlank(message = "Debe indicarse quién reporta la necesidad")
    @Schema(description = "Persona, municipalidad o entidad que reporta la necesidad", example = "Municipalidad de Til Til")
    val reportedBy: String
)
 
@Schema(description = "Objeto de petición para actualizar el estado de una necesidad")
data class UpdateNeedStatusDto(
    @field:NotNull(message = "El nuevo estado es obligatorio")
    @Schema(description = "Nuevo estado de la necesidad", example = "IN_PROGRESS")
    val status: NeedStatus
)
