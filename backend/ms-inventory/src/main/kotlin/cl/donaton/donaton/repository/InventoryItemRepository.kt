package cl.donaton.donaton.repository
 
import cl.donaton.donaton.model.InventoryItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
 
@Repository
interface InventoryItemRepository : JpaRepository<InventoryItem, Long> {
    fun findByCollectionCenterId(collectionCenterId: Long): List<InventoryItem>
    fun findByCollectionCenterIdAndResource(collectionCenterId: Long, resource: String): InventoryItem?
}
