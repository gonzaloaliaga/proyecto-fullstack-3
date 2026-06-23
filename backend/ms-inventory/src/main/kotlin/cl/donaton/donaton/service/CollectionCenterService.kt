package cl.donaton.donaton.service
 
import cl.donaton.donaton.dto.CreateCollectionCenterDto
import cl.donaton.donaton.dto.UpdateCollectionCenterDto
import cl.donaton.donaton.exception.NotFoundException
import cl.donaton.donaton.model.CollectionCenter
import cl.donaton.donaton.repository.CollectionCenterRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
 
@Service
class CollectionCenterService(
    private val collectionCenterRepository: CollectionCenterRepository
) {
 
    @Transactional
    fun createCollectionCenter(dto: CreateCollectionCenterDto): CollectionCenter {
        val center = CollectionCenter(
            name = dto.name,
            address = dto.address,
            region = dto.region,
            capacity = dto.capacity,
            active = true
        )
        return collectionCenterRepository.save(center)
    }
 
    @Transactional(readOnly = true)
    fun getCollectionCenterById(id: Long): CollectionCenter {
        return collectionCenterRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Centro de acopio no encontrado")
    }
 
    @Transactional(readOnly = true)
    fun getAllCollectionCenters(onlyActive: Boolean): List<CollectionCenter> {
        return if (onlyActive) {
            collectionCenterRepository.findByActiveTrue()
        } else {
            collectionCenterRepository.findAll()
        }
    }
 
    @Transactional(readOnly = true)
    fun getCollectionCentersByRegion(region: String): List<CollectionCenter> {
        return collectionCenterRepository.findByRegion(region)
    }
 
    @Transactional
    fun updateCollectionCenter(id: Long, dto: UpdateCollectionCenterDto): CollectionCenter {
        val existing = collectionCenterRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Centro de acopio no encontrado")
 
        val updated = existing.copy(
            name = dto.name ?: existing.name,
            address = dto.address ?: existing.address,
            region = dto.region ?: existing.region,
            capacity = dto.capacity ?: existing.capacity,
            active = dto.active ?: existing.active
        )
        return collectionCenterRepository.save(updated)
    }
 
    @Transactional
    fun deleteCollectionCenter(id: Long) {
        val existing = collectionCenterRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Centro de acopio no encontrado")
        collectionCenterRepository.delete(existing)
    }
}
