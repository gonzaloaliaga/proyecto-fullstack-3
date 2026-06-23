package cl.donaton.donaton.service
 
import cl.donaton.donaton.dto.AssignTransportDto
import cl.donaton.donaton.dto.CreateShipmentDto
import cl.donaton.donaton.dto.UpdateShipmentStatusDto
import cl.donaton.donaton.exception.BadRequestException
import cl.donaton.donaton.exception.NotFoundException
import cl.donaton.donaton.exception.TransportUnavailableException
import cl.donaton.donaton.model.NeedStatus
import cl.donaton.donaton.model.Shipment
import cl.donaton.donaton.model.ShipmentStatus
import cl.donaton.donaton.repository.NeedRepository
import cl.donaton.donaton.repository.ShipmentRepository
import cl.donaton.donaton.repository.TransportRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
 
@Service
class ShipmentService(
    private val shipmentRepository: ShipmentRepository,
    private val needRepository: NeedRepository,
    private val transportRepository: TransportRepository
) {
 
    @Transactional
    fun createShipment(dto: CreateShipmentDto): Shipment {
        val need = dto.needId?.let {
            needRepository.findByIdOrNull(it)
                ?: throw NotFoundException("Necesidad no encontrada")
        }
 
        val transport = dto.transportId?.let { transportId ->
            val foundTransport = transportRepository.findByIdOrNull(transportId)
                ?: throw NotFoundException("Transporte no encontrado")
            if (!foundTransport.available) {
                throw TransportUnavailableException("El transporte seleccionado no está disponible")
            }
            transportRepository.save(foundTransport.copy(available = false))
        }
 
        val shipment = Shipment(
            donationId = dto.donationId,
            need = need,
            transport = transport,
            originCenterId = dto.originCenterId,
            destination = dto.destination,
            status = ShipmentStatus.PLANNED,
            scheduledDate = dto.scheduledDate
        )
 
        val savedShipment = shipmentRepository.save(shipment)
 
        if (need != null && need.status == NeedStatus.REPORTED) {
            needRepository.save(need.copy(status = NeedStatus.IN_PROGRESS))
        }
 
        return savedShipment
    }
 
    @Transactional(readOnly = true)
    fun getShipmentById(id: Long): Shipment {
        return shipmentRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Envío no encontrado")
    }
 
    @Transactional(readOnly = true)
    fun getAllShipments(): List<Shipment> {
        return shipmentRepository.findAll()
    }
 
    @Transactional(readOnly = true)
    fun getShipmentsByStatus(status: ShipmentStatus): List<Shipment> {
        return shipmentRepository.findByStatus(status)
    }
 
    @Transactional(readOnly = true)
    fun getShipmentsByOriginCenter(originCenterId: Long): List<Shipment> {
        return shipmentRepository.findByOriginCenterId(originCenterId)
    }

    @Transactional
    fun assignTransport(id: Long, dto: AssignTransportDto): Shipment {
        val existingShipment = shipmentRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Envío no encontrado")
 
        if (existingShipment.status == ShipmentStatus.DELIVERED || existingShipment.status == ShipmentStatus.CANCELLED) {
            throw BadRequestException("No se puede asignar transporte a un envío ${existingShipment.status}")
        }
 
        val newTransport = transportRepository.findByIdOrNull(dto.transportId)
            ?: throw NotFoundException("Transporte no encontrado")
 
        if (!newTransport.available) {
            throw TransportUnavailableException("El transporte seleccionado no está disponible")
        }
 
        existingShipment.transport?.let { previousTransport ->
            transportRepository.save(previousTransport.copy(available = true))
        }
 
        val reservedTransport = transportRepository.save(newTransport.copy(available = false))
        val updatedShipment = existingShipment.copy(transport = reservedTransport)
 
        return shipmentRepository.save(updatedShipment)
    }
 
    @Transactional
    fun updateShipmentStatus(id: Long, dto: UpdateShipmentStatusDto): Shipment {
        val existingShipment = shipmentRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Envío no encontrado")
 
        if (existingShipment.status == ShipmentStatus.DELIVERED || existingShipment.status == ShipmentStatus.CANCELLED) {
            throw BadRequestException("No se puede modificar un envío que ya está ${existingShipment.status}")
        }
 
        val updatedShipment = when (dto.status) {
            ShipmentStatus.DELIVERED -> {
                existingShipment.transport?.let { transportRepository.save(it.copy(available = true)) }
                existingShipment.need?.let { needRepository.save(it.copy(status = NeedStatus.COVERED)) }
                existingShipment.copy(status = ShipmentStatus.DELIVERED, deliveredAt = LocalDateTime.now())
            }
            ShipmentStatus.CANCELLED -> {
                existingShipment.transport?.let { transportRepository.save(it.copy(available = true)) }
                existingShipment.copy(status = ShipmentStatus.CANCELLED)
            }
            else -> existingShipment.copy(status = dto.status)
        }
 
        return shipmentRepository.save(updatedShipment)
    }
 
    @Transactional
    fun deleteShipment(id: Long) {
        val existingShipment = shipmentRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Envío no encontrado")
 
        existingShipment.transport?.let { transportRepository.save(it.copy(available = true)) }
 
        shipmentRepository.delete(existingShipment)
    }
}
