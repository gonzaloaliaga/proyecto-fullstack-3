package cl.donaton.donaton.repository
 
import cl.donaton.donaton.model.Transport
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
 
@Repository
interface TransportRepository : JpaRepository<Transport, Long> {
    fun findByAvailableTrue(): List<Transport>
}
