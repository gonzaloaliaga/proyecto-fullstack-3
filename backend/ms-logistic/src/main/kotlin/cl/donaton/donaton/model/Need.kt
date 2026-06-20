package cl.donaton.donaton.model
 
import jakarta.persistence.*
import java.time.LocalDateTime
 
@Entity
@Table(name = "needs")
data class Need(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
 
    @Column(nullable = false)
    val resource: String,
 
    @Column(nullable = false)
    val quantity: Int,
 
    @Column(nullable = false)
    val location: String,
 
    @Column(nullable = true)
    val latitude: Double? = null,
 
    @Column(nullable = true)
    val longitude: Double? = null,
 
    @Column(name = "reported_by", nullable = false)
    val reportedBy: String,
 
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: NeedStatus = NeedStatus.REPORTED,
 
    @Column(name = "reported_at", nullable = false)
    val reportedAt: LocalDateTime = LocalDateTime.now()
) {
    /* Constructor vacío requerido por JPA */
    constructor() : this(0, "", 0, "", null, null, "", NeedStatus.REPORTED, LocalDateTime.now())
}
 
enum class NeedStatus {
    REPORTED,
    IN_PROGRESS,
    COVERED,
    CANCELLED
}
