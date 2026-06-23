package cl.donaton.donaton.repository
 
import cl.donaton.donaton.model.CollectionCenter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
 
@Repository
interface CollectionCenterRepository : JpaRepository<CollectionCenter, Long> {
    fun findByActiveTrue(): List<CollectionCenter>
    fun findByRegion(region: String): List<CollectionCenter>
}
