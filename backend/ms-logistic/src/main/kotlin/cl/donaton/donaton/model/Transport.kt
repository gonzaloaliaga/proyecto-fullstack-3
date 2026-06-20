package cl.donaton.donaton.model
 
import jakarta.persistence.*
 
@Entity
@Table(name = "transports")
data class Transport(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
 
    @Column(name = "vehicle_plate", nullable = false)
    val vehiclePlate: String,
 
    @Column(name = "vehicle_type", nullable = false)
    val vehicleType: String,
 
    @Column(name = "driver_name", nullable = false)
    val driverName: String,
 
    @Column(nullable = false)
    val capacity: Int,
 
    @Column(nullable = false)
    val available: Boolean = true
) {
    /* Constructor vacío requerido por JPA */
    constructor() : this(0, "", "", "", 0, true)
}
