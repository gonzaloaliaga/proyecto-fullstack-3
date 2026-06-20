package cl.donaton.donaton.repository
 
import cl.donaton.donaton.model.Need
import cl.donaton.donaton.model.NeedStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
 
@Repository
interface NeedRepository : JpaRepository<Need, Long> {
    fun findByStatus(status: NeedStatus): List<Need>
    fun findByLocation(location: String): List<Need>
}
