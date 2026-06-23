package cl.donaton.donaton.dto
 
import cl.donaton.donaton.model.DonationStatus
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import java.time.LocalDate
 
@Schema(description = "Objeto de petición para registrar una nueva donación")
data class CreateDonationDto(
    @field:NotBlank(message = "El recurso donado no puede estar vacío")
    @Schema(description = "Tipo de recurso donado", example = "Ropa de invierno")
    val resource: String,
 
    @field:Min(value = 1, message = "La cantidad debe ser mayor a 0")
    @Schema(description = "Cantidad de unidades donadas", example = "50")
    val quantity: Int,
 
    @field:NotBlank(message = "El origen de la donación no puede estar vacío")
    @Schema(description = "Origen de la donación (persona, empresa o municipalidad)", example = "Empresa TextilSur SpA")
    val origin: String,
 
    @field:NotNull(message = "La fecha de donación es obligatoria")
    @field:PastOrPresent(message = "La fecha de donación no puede ser futura")
    @Schema(description = "Fecha en que se realizó la donación", example = "2026-03-10")
    val donationDate: LocalDate,
 
    @field:NotNull(message = "El centro de acopio asignado es obligatorio")
    @Schema(description = "ID del centro de acopio (casa) donde llega el recurso", example = "1")
    val collectionCenterId: Long
)
 
@Schema(description = "Objeto de petición para actualizar el estado de una donación")
data class UpdateDonationStatusDto(
    @field:NotNull(message = "El nuevo estado es obligatorio")
    @Schema(description = "Nuevo estado del ciclo de vida de la donación", example = "RECEIVED")
    val status: DonationStatus
)
