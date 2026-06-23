package cl.donaton.donaton.model
 
import jakarta.persistence.*
import java.time.LocalDate
 
@Entity
@Table(name = "donations")
data class Donation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
 
    @Column(nullable = false)
    val resource: String,
 
    @Column(nullable = false)
    val quantity: Int,
 
    @Column(nullable = false)
    val origin: String,
 
    @Column(name = "donation_date", nullable = false)
    val donationDate: LocalDate,
 
    @Column(name = "collection_center_id", nullable = false)
    val collectionCenterId: Long,
 
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: DonationStatus = DonationStatus.PENDING
) {
    constructor() : this(0, "", 0, "", LocalDate.now(), 0, DonationStatus.PENDING)
}
 
enum class DonationStatus {
    PENDING,
    RECEIVED,
    ASSIGNED,
    DELIVERED
}
