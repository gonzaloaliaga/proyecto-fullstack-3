package cl.donaton.donaton.repository
 
import cl.donaton.donaton.model.Donation
import cl.donaton.donaton.model.DonationStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
 
@Repository
interface DonationRepository : JpaRepository<Donation, Long> {
    fun findByCollectionCenterId(collectionCenterId: Long): List<Donation>
    fun findByStatus(status: DonationStatus): List<Donation>
}
