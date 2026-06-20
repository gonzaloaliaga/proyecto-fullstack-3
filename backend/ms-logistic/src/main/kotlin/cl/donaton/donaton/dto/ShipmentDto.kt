package cl.donaton.donaton.dto
 
import cl.donaton.donaton.model.ShipmentStatus
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
 
@Schema(description = "Objeto de petición para planificar un nuevo envío")
data class CreateShipmentDto(
    @Schema(description = "ID de la donación (en ms-donation) que se está enviando", example = "1")
    val donationId: Long? = null,
 
    @Schema(description = "ID de la necesidad (registrada en este servicio) que el envío busca cubrir", example = "1")
    val needId: Long? = null,
 
    @Schema(description = "ID del transporte asignado al envío (opcional al momento de crear)", example = "1")
    val transportId: Long? = null,
 
    @field:NotNull(message = "El centro de acopio de origen es obligatorio")
    @Schema(description = "ID del centro de acopio (en ms-inventory) desde donde sale el envío", example = "1")
    val originCenterId: Long,
 
    @field:NotBlank(message = "El destino no puede estar vacío")
    @Schema(description = "Destino final del envío", example = "Comuna de Til Til")
    val destination: String,
 
    @field:NotNull(message = "La fecha programada es obligatoria")
    @field:FutureOrPresent(message = "La fecha programada no puede ser pasada")
    @Schema(description = "Fecha programada para el envío", example = "2026-04-01")
    val scheduledDate: LocalDate
)
 
@Schema(description = "Objeto de petición para asignar o reasignar un transporte a un envío")
data class AssignTransportDto(
    @field:NotNull(message = "El ID del transporte es obligatorio")
    @Schema(description = "ID del transporte a asignar", example = "1")
    val transportId: Long
)
 
@Schema(description = "Objeto de petición para actualizar el estado de un envío")
data class UpdateShipmentStatusDto(
    @field:NotNull(message = "El nuevo estado es obligatorio")
    @Schema(description = "Nuevo estado del ciclo de vida del envío", example = "IN_TRANSIT")
    val status: ShipmentStatus
)
