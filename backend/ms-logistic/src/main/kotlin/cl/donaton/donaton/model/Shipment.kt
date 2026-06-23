package cl.donaton.donaton.model
 
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "shipments")
data class Shipment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
 
    @Column(name = "donation_id", nullable = true)
    val donationId: Long? = null,
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "need_id", nullable = true)
    val need: Need? = null,
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport_id", nullable = true)
    val transport: Transport? = null,
 
    @Column(name = "origin_center_id", nullable = false)
    val originCenterId: Long,
 
    @Column(nullable = false)
    val destination: String,
 
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: ShipmentStatus = ShipmentStatus.PLANNED,
 
    @Column(name = "scheduled_date", nullable = false)
    val scheduledDate: LocalDate,
 
    @Column(name = "delivered_at", nullable = true)
    val deliveredAt: LocalDateTime? = null
) {
    constructor() : this(0, null, null, null, 0, "", ShipmentStatus.PLANNED, LocalDate.now(), null)
}
 
enum class ShipmentStatus {
    PLANNED,
    IN_TRANSIT,
    DELIVERED,
    CANCELLED
}
