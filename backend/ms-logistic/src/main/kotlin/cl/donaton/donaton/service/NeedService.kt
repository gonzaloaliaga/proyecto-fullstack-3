package cl.donaton.donaton.service
 
import cl.donaton.donaton.dto.CreateNeedDto
import cl.donaton.donaton.dto.UpdateNeedStatusDto
import cl.donaton.donaton.exception.NotFoundException
import cl.donaton.donaton.model.Need
import cl.donaton.donaton.model.NeedStatus
import cl.donaton.donaton.repository.NeedRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
 
@Service
class NeedService(
    private val needRepository: NeedRepository
) {
 
    @Transactional
    fun createNeed(dto: CreateNeedDto): Need {
        val need = Need(
            resource = dto.resource,
            quantity = dto.quantity,
            location = dto.location,
            latitude = dto.latitude,
            longitude = dto.longitude,
            reportedBy = dto.reportedBy,
            status = NeedStatus.REPORTED,
            reportedAt = LocalDateTime.now()
        )
        return needRepository.save(need)
    }
 
    @Transactional(readOnly = true)
    fun getNeedById(id: Long): Need {
        return needRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Necesidad no encontrada")
    }
 
    @Transactional(readOnly = true)
    fun getAllNeeds(): List<Need> {
        return needRepository.findAll()
    }
 
    @Transactional(readOnly = true)
    fun getNeedsByStatus(status: NeedStatus): List<Need> {
        return needRepository.findByStatus(status)
    }
 
    @Transactional(readOnly = true)
    fun getNeedsByLocation(location: String): List<Need> {
        return needRepository.findByLocation(location)
    }
 
    @Transactional
    fun updateNeedStatus(id: Long, dto: UpdateNeedStatusDto): Need {
        val existing = needRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Necesidad no encontrada")
 
        val updated = existing.copy(status = dto.status)
        return needRepository.save(updated)
    }
 
    @Transactional
    fun deleteNeed(id: Long) {
        val existing = needRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Necesidad no encontrada")
        needRepository.delete(existing)
    }
}
