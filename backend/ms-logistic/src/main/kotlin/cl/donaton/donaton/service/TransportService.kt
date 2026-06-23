package cl.donaton.donaton.service
 
import cl.donaton.donaton.dto.CreateTransportDto
import cl.donaton.donaton.exception.NotFoundException
import cl.donaton.donaton.model.Transport
import cl.donaton.donaton.repository.TransportRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
 
@Service
class TransportService(
    private val transportRepository: TransportRepository
) {
 
    @Transactional
    fun createTransport(dto: CreateTransportDto): Transport {
        val transport = Transport(
            vehiclePlate = dto.vehiclePlate,
            vehicleType = dto.vehicleType,
            driverName = dto.driverName,
            capacity = dto.capacity,
            available = true
        )
        return transportRepository.save(transport)
    }
 
    @Transactional(readOnly = true)
    fun getTransportById(id: Long): Transport {
        return transportRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Transporte no encontrado")
    }
 
    @Transactional(readOnly = true)
    fun getAllTransports(onlyAvailable: Boolean): List<Transport> {
        return if (onlyAvailable) {
            transportRepository.findByAvailableTrue()
        } else {
            transportRepository.findAll()
        }
    }
 
    @Transactional
    fun setTransportAvailability(id: Long, available: Boolean): Transport {
        val existing = transportRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Transporte no encontrado")
 
        val updated = existing.copy(available = available)
        return transportRepository.save(updated)
    }
 
    @Transactional
    fun deleteTransport(id: Long) {
        val existing = transportRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Transporte no encontrado")
        transportRepository.delete(existing)
    }
}
