package cl.donaton.donaton.dto
 
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
 
@Schema(description = "Objeto de petición para registrar un nuevo transporte disponible")
data class CreateTransportDto(
    @field:NotBlank(message = "La patente del vehículo no puede estar vacía")
    @Schema(description = "Patente del vehículo", example = "ABCD12")
    val vehiclePlate: String,
 
    @field:NotBlank(message = "El tipo de vehículo no puede estar vacío")
    @Schema(description = "Tipo de vehículo", example = "Camión")
    val vehicleType: String,
 
    @field:NotBlank(message = "El nombre del conductor no puede estar vacío")
    @Schema(description = "Nombre del conductor asignado", example = "Pedro González")
    val driverName: String,
 
    @field:Min(value = 1, message = "La capacidad debe ser mayor a 0")
    @Schema(description = "Capacidad de carga del vehículo", example = "1000")
    val capacity: Int
)
