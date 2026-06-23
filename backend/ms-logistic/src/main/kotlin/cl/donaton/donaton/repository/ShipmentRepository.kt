package cl.donaton.donaton.repository
 
import cl.donaton.donaton.model.Shipment
import cl.donaton.donaton.model.ShipmentStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
 
@Repository
interface ShipmentRepository : JpaRepository<Shipment, Long> {
    fun findByStatus(status: ShipmentStatus): List<Shipment>
    fun findByOriginCenterId(originCenterId: Long): List<Shipment>
    fun findByNeedId(needId: Long): List<Shipment>
}
