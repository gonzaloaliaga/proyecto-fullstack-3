package cl.donaton.donaton.service
 
import cl.donaton.donaton.dto.AssignTransportDto
import cl.donaton.donaton.dto.CreateShipmentDto
import cl.donaton.donaton.dto.UpdateShipmentStatusDto
import cl.donaton.donaton.exception.BadRequestException
import cl.donaton.donaton.exception.NotFoundException
import cl.donaton.donaton.exception.TransportUnavailableException
import cl.donaton.donaton.model.Need
import cl.donaton.donaton.model.NeedStatus
import cl.donaton.donaton.model.Shipment
import cl.donaton.donaton.model.ShipmentStatus
import cl.donaton.donaton.model.Transport
import cl.donaton.donaton.repository.NeedRepository
import cl.donaton.donaton.repository.ShipmentRepository
import cl.donaton.donaton.repository.TransportRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
 
class ShipmentServiceTest {
 
    private val shipmentRepository = mockk<ShipmentRepository>()
    private val needRepository = mockk<NeedRepository>()
    private val transportRepository = mockk<TransportRepository>()
    private val service = ShipmentService(shipmentRepository, needRepository, transportRepository)
 
    private val transporteDisponible = Transport(
        id = 1L,
        vehiclePlate = "ABCD12",
        vehicleType = "Camión",
        driverName = "Pedro González",
        capacity = 1000,
        available = true
    )
 
    private val transporteOcupado = transporteDisponible.copy(available = false)
 
    private val necesidadReportada = Need(
        id = 1L,
        resource = "Agua potable",
        quantity = 500,
        location = "Comuna de Til Til",
        reportedBy = "Municipalidad de Til Til",
        status = NeedStatus.REPORTED
    )
 
    private val envioBase = Shipment(
        id = 1L,
        donationId = 1L,
        need = null,
        transport = null,
        originCenterId = 1L,
        destination = "Comuna de Til Til",
        status = ShipmentStatus.PLANNED,
        scheduledDate = LocalDate.of(2026, 4, 1)
    )
 
    // ── CREATE ─────────────────────────────────────────────────────────────
 
    @Test
    fun `createShipment sin transporte ni necesidad debe crear el envio en estado PLANNED`() {
        val dto = CreateShipmentDto(
            donationId = 1L,
            needId = null,
            transportId = null,
            originCenterId = 1L,
            destination = "Comuna de Til Til",
            scheduledDate = LocalDate.of(2026, 4, 1)
        )
 
        every { shipmentRepository.save(any()) } returns envioBase
 
        val result = service.createShipment(dto)
 
        assertEquals(ShipmentStatus.PLANNED, result.status)
        assertNull(result.transport)
    }
 
    @Test
    fun `createShipment con transporte disponible debe reservarlo`() {
        val dto = CreateShipmentDto(
            donationId = 1L,
            transportId = 1L,
            originCenterId = 1L,
            destination = "Comuna de Til Til",
            scheduledDate = LocalDate.of(2026, 4, 1)
        )
        val envioConTransporte = envioBase.copy(transport = transporteOcupado)
 
        every { transportRepository.findByIdOrNull(1L) } returns transporteDisponible
        every { transportRepository.save(transporteOcupado) } returns transporteOcupado
        every { shipmentRepository.save(any()) } returns envioConTransporte
 
        val result = service.createShipment(dto)
 
        assertNotNull(result.transport)
        verify(exactly = 1) { transportRepository.save(transporteOcupado) }
    }
 
    @Test
    fun `createShipment con transporte no disponible debe lanzar TransportUnavailableException`() {
        val dto = CreateShipmentDto(
            transportId = 1L,
            originCenterId = 1L,
            destination = "Comuna de Til Til",
            scheduledDate = LocalDate.of(2026, 4, 1)
        )
 
        every { transportRepository.findByIdOrNull(1L) } returns transporteOcupado
 
        assertThrows<TransportUnavailableException> {
            service.createShipment(dto)
        }
    }
 
    @Test
    fun `createShipment con transporte inexistente debe lanzar NotFoundException`() {
        val dto = CreateShipmentDto(
            transportId = 99L,
            originCenterId = 1L,
            destination = "Comuna de Til Til",
            scheduledDate = LocalDate.of(2026, 4, 1)
        )
 
        every { transportRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            service.createShipment(dto)
        }
    }
 
    @Test
    fun `createShipment con necesidad inexistente debe lanzar NotFoundException`() {
        val dto = CreateShipmentDto(
            needId = 99L,
            originCenterId = 1L,
            destination = "Comuna de Til Til",
            scheduledDate = LocalDate.of(2026, 4, 1)
        )
 
        every { needRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            service.createShipment(dto)
        }
    }
 
    @Test
    fun `createShipment con necesidad REPORTED debe marcarla IN_PROGRESS`() {
        val dto = CreateShipmentDto(
            needId = 1L,
            originCenterId = 1L,
            destination = "Comuna de Til Til",
            scheduledDate = LocalDate.of(2026, 4, 1)
        )
        val necesidadEnProgreso = necesidadReportada.copy(status = NeedStatus.IN_PROGRESS)
        val envioConNecesidad = envioBase.copy(need = necesidadReportada)
 
        every { needRepository.findByIdOrNull(1L) } returns necesidadReportada
        every { shipmentRepository.save(any()) } returns envioConNecesidad
        every { needRepository.save(necesidadEnProgreso) } returns necesidadEnProgreso
 
        service.createShipment(dto)
 
        verify(exactly = 1) { needRepository.save(necesidadEnProgreso) }
    }
 
    // ── GET ────────────────────────────────────────────────────────────────
 
    @Test
    fun `getShipmentById existente debe retornar el envio`() {
        every { shipmentRepository.findByIdOrNull(1L) } returns envioBase
 
        val result = service.getShipmentById(1L)
 
        assertEquals(envioBase, result)
    }
 
    @Test
    fun `getShipmentById inexistente debe lanzar NotFoundException`() {
        every { shipmentRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            service.getShipmentById(99L)
        }
    }
 
    @Test
    fun `getAllShipments debe retornar todos los envios`() {
        every { shipmentRepository.findAll() } returns listOf(envioBase)
 
        val result = service.getAllShipments()
 
        assertEquals(1, result.size)
    }
 
    @Test
    fun `getShipmentsByStatus debe filtrar por estado`() {
        every { shipmentRepository.findByStatus(ShipmentStatus.PLANNED) } returns listOf(envioBase)
 
        val result = service.getShipmentsByStatus(ShipmentStatus.PLANNED)
 
        assertEquals(1, result.size)
    }
 
    @Test
    fun `getShipmentsByOriginCenter debe filtrar por centro de origen`() {
        every { shipmentRepository.findByOriginCenterId(1L) } returns listOf(envioBase)
 
        val result = service.getShipmentsByOriginCenter(1L)
 
        assertEquals(1, result.size)
    }
 
    // ── ASSIGN TRANSPORT ───────────────────────────────────────────────────
 
    @Test
    fun `assignTransport sin transporte previo debe reservar el nuevo transporte`() {
        val dto = AssignTransportDto(transportId = 1L)
        val envioConTransporte = envioBase.copy(transport = transporteOcupado)
 
        every { shipmentRepository.findByIdOrNull(1L) } returns envioBase
        every { transportRepository.findByIdOrNull(1L) } returns transporteDisponible
        every { transportRepository.save(transporteOcupado) } returns transporteOcupado
        every { shipmentRepository.save(envioConTransporte) } returns envioConTransporte
 
        val result = service.assignTransport(1L, dto)
 
        assertEquals(transporteOcupado, result.transport)
    }
 
    @Test
    fun `assignTransport con transporte previo debe liberar el anterior y reservar el nuevo`() {
        val transporteAnterior = transporteDisponible.copy(id = 2L, available = false)
        val envioConTransporteAnterior = envioBase.copy(transport = transporteAnterior)
        val dto = AssignTransportDto(transportId = 1L)
 
        every { shipmentRepository.findByIdOrNull(1L) } returns envioConTransporteAnterior
        every { transportRepository.findByIdOrNull(1L) } returns transporteDisponible
        every { transportRepository.save(transporteAnterior.copy(available = true)) } returns transporteAnterior.copy(available = true)
        every { transportRepository.save(transporteOcupado) } returns transporteOcupado
        every { shipmentRepository.save(any()) } returns envioConTransporteAnterior.copy(transport = transporteOcupado)
 
        service.assignTransport(1L, dto)
 
        verify(exactly = 1) { transportRepository.save(transporteAnterior.copy(available = true)) }
        verify(exactly = 1) { transportRepository.save(transporteOcupado) }
    }
 
    @Test
    fun `assignTransport con transporte no disponible debe lanzar TransportUnavailableException`() {
        every { shipmentRepository.findByIdOrNull(1L) } returns envioBase
        every { transportRepository.findByIdOrNull(1L) } returns transporteOcupado
 
        assertThrows<TransportUnavailableException> {
            service.assignTransport(1L, AssignTransportDto(transportId = 1L))
        }
    }
 
    @Test
    fun `assignTransport sobre envio DELIVERED debe lanzar BadRequestException`() {
        val envioEntregado = envioBase.copy(status = ShipmentStatus.DELIVERED)
 
        every { shipmentRepository.findByIdOrNull(1L) } returns envioEntregado
 
        assertThrows<BadRequestException> {
            service.assignTransport(1L, AssignTransportDto(transportId = 1L))
        }
    }
 
    @Test
    fun `assignTransport con envio inexistente debe lanzar NotFoundException`() {
        every { shipmentRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            service.assignTransport(99L, AssignTransportDto(transportId = 1L))
        }
    }
 
    // ── UPDATE STATUS ──────────────────────────────────────────────────────
 
    @Test
    fun `updateShipmentStatus a IN_TRANSIT debe actualizar el estado sin tocar transporte`() {
        val dto = UpdateShipmentStatusDto(status = ShipmentStatus.IN_TRANSIT)
        val actualizado = envioBase.copy(status = ShipmentStatus.IN_TRANSIT)
 
        every { shipmentRepository.findByIdOrNull(1L) } returns envioBase
        every { shipmentRepository.save(actualizado) } returns actualizado
 
        val result = service.updateShipmentStatus(1L, dto)
 
        assertEquals(ShipmentStatus.IN_TRANSIT, result.status)
    }
 
    @Test
    fun `updateShipmentStatus a DELIVERED debe liberar transporte y marcar fecha de entrega`() {
        val envioConTransporte = envioBase.copy(transport = transporteOcupado)
        val dto = UpdateShipmentStatusDto(status = ShipmentStatus.DELIVERED)
        val transporteLiberado = transporteOcupado.copy(available = true)
        val capturedShipment = slot<Shipment>()
 
        every { shipmentRepository.findByIdOrNull(1L) } returns envioConTransporte
        every { transportRepository.save(transporteLiberado) } returns transporteLiberado
        every { shipmentRepository.save(capture(capturedShipment)) } answers { capturedShipment.captured }
 
        val result = service.updateShipmentStatus(1L, dto)
 
        assertEquals(ShipmentStatus.DELIVERED, result.status)
        assertNotNull(result.deliveredAt)
        verify(exactly = 1) { transportRepository.save(transporteLiberado) }
    }
 
    @Test
    fun `updateShipmentStatus a DELIVERED con necesidad asociada debe marcarla COVERED`() {
        val necesidadEnProgreso = necesidadReportada.copy(status = NeedStatus.IN_PROGRESS)
        val envioConNecesidad = envioBase.copy(need = necesidadEnProgreso)
        val dto = UpdateShipmentStatusDto(status = ShipmentStatus.DELIVERED)
        val necesidadCubierta = necesidadEnProgreso.copy(status = NeedStatus.COVERED)
        val capturedShipment = slot<Shipment>()
 
        every { shipmentRepository.findByIdOrNull(1L) } returns envioConNecesidad
        every { needRepository.save(necesidadCubierta) } returns necesidadCubierta
        every { shipmentRepository.save(capture(capturedShipment)) } answers { capturedShipment.captured }
 
        service.updateShipmentStatus(1L, dto)
 
        verify(exactly = 1) { needRepository.save(necesidadCubierta) }
    }
 
    @Test
    fun `updateShipmentStatus a CANCELLED debe liberar el transporte asignado`() {
        val envioConTransporte = envioBase.copy(transport = transporteOcupado)
        val dto = UpdateShipmentStatusDto(status = ShipmentStatus.CANCELLED)
        val transporteLiberado = transporteOcupado.copy(available = true)
        val capturedShipment = slot<Shipment>()
 
        every { shipmentRepository.findByIdOrNull(1L) } returns envioConTransporte
        every { transportRepository.save(transporteLiberado) } returns transporteLiberado
        every { shipmentRepository.save(capture(capturedShipment)) } answers { capturedShipment.captured }
 
        val result = service.updateShipmentStatus(1L, dto)
 
        assertEquals(ShipmentStatus.CANCELLED, result.status)
        verify(exactly = 1) { transportRepository.save(transporteLiberado) }
    }
 
    @Test
    fun `updateShipmentStatus sobre envio ya DELIVERED debe lanzar BadRequestException`() {
        val envioEntregado = envioBase.copy(status = ShipmentStatus.DELIVERED)
 
        every { shipmentRepository.findByIdOrNull(1L) } returns envioEntregado
 
        assertThrows<BadRequestException> {
            service.updateShipmentStatus(1L, UpdateShipmentStatusDto(ShipmentStatus.IN_TRANSIT))
        }
    }
 
    @Test
    fun `updateShipmentStatus sobre envio ya CANCELLED debe lanzar BadRequestException`() {
        val envioCancelado = envioBase.copy(status = ShipmentStatus.CANCELLED)
 
        every { shipmentRepository.findByIdOrNull(1L) } returns envioCancelado
 
        assertThrows<BadRequestException> {
            service.updateShipmentStatus(1L, UpdateShipmentStatusDto(ShipmentStatus.DELIVERED))
        }
    }
 
    @Test
    fun `updateShipmentStatus con envio inexistente debe lanzar NotFoundException`() {
        every { shipmentRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            service.updateShipmentStatus(99L, UpdateShipmentStatusDto(ShipmentStatus.IN_TRANSIT))
        }
    }
 
    // ── DELETE ─────────────────────────────────────────────────────────────
 
    @Test
    fun `deleteShipment sin transporte asignado debe eliminar el envio directamente`() {
        every { shipmentRepository.findByIdOrNull(1L) } returns envioBase
        every { shipmentRepository.delete(envioBase) } returns Unit
 
        service.deleteShipment(1L)
 
        verify(exactly = 1) { shipmentRepository.delete(envioBase) }
    }
 
    @Test
    fun `deleteShipment con transporte asignado debe liberarlo antes de eliminar`() {
        val envioConTransporte = envioBase.copy(transport = transporteOcupado)
        val transporteLiberado = transporteOcupado.copy(available = true)
 
        every { shipmentRepository.findByIdOrNull(1L) } returns envioConTransporte
        every { transportRepository.save(transporteLiberado) } returns transporteLiberado
        every { shipmentRepository.delete(envioConTransporte) } returns Unit
 
        service.deleteShipment(1L)
 
        verify(exactly = 1) { transportRepository.save(transporteLiberado) }
        verify(exactly = 1) { shipmentRepository.delete(envioConTransporte) }
    }
 
    @Test
    fun `deleteShipment con id inexistente debe lanzar NotFoundException`() {
        every { shipmentRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            service.deleteShipment(99L)
        }
    }
}
