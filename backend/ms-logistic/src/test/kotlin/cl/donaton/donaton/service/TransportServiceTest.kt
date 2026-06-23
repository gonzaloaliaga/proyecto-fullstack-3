package cl.donaton.donaton.service
 
import cl.donaton.donaton.dto.CreateTransportDto
import cl.donaton.donaton.exception.NotFoundException
import cl.donaton.donaton.model.Transport
import cl.donaton.donaton.repository.TransportRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.assertEquals
 
class TransportServiceTest {
 
    private val transportRepository = mockk<TransportRepository>()
    private val service = TransportService(transportRepository)
 
    private val transporteBase = Transport(
        id = 1L,
        vehiclePlate = "ABCD12",
        vehicleType = "Camión",
        driverName = "Pedro González",
        capacity = 1000,
        available = true
    )
 
    // ── CREATE ─────────────────────────────────────────────────────────────
 
    @Test
    fun `createTransport debe guardar y retornar el transporte como disponible`() {
        val dto = CreateTransportDto(
            vehiclePlate = "ABCD12",
            vehicleType = "Camión",
            driverName = "Pedro González",
            capacity = 1000
        )
 
        every { transportRepository.save(any()) } returns transporteBase
 
        val result = service.createTransport(dto)
 
        assertEquals(true, result.available)
        assertEquals("ABCD12", result.vehiclePlate)
    }
 
    // ── GET ────────────────────────────────────────────────────────────────
 
    @Test
    fun `getTransportById existente debe retornar el transporte`() {
        every { transportRepository.findByIdOrNull(1L) } returns transporteBase
 
        val result = service.getTransportById(1L)
 
        assertEquals(transporteBase, result)
    }
 
    @Test
    fun `getTransportById inexistente debe lanzar NotFoundException`() {
        every { transportRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            service.getTransportById(99L)
        }
    }
 
    @Test
    fun `getAllTransports con onlyAvailable false debe retornar todos`() {
        every { transportRepository.findAll() } returns listOf(transporteBase)
 
        val result = service.getAllTransports(onlyAvailable = false)
 
        assertEquals(1, result.size)
        verify(exactly = 1) { transportRepository.findAll() }
    }
 
    @Test
    fun `getAllTransports con onlyAvailable true debe filtrar solo disponibles`() {
        every { transportRepository.findByAvailableTrue() } returns listOf(transporteBase)
 
        val result = service.getAllTransports(onlyAvailable = true)
 
        assertEquals(1, result.size)
        verify(exactly = 1) { transportRepository.findByAvailableTrue() }
    }
 
    // ── AVAILABILITY ───────────────────────────────────────────────────────
 
    @Test
    fun `setTransportAvailability debe marcar el transporte como no disponible`() {
        val actualizado = transporteBase.copy(available = false)
 
        every { transportRepository.findByIdOrNull(1L) } returns transporteBase
        every { transportRepository.save(actualizado) } returns actualizado
 
        val result = service.setTransportAvailability(1L, false)
 
        assertEquals(false, result.available)
    }
 
    @Test
    fun `setTransportAvailability con id inexistente debe lanzar NotFoundException`() {
        every { transportRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            service.setTransportAvailability(99L, false)
        }
    }
 
    // ── DELETE ─────────────────────────────────────────────────────────────
 
    @Test
    fun `deleteTransport existente debe eliminar el transporte`() {
        every { transportRepository.findByIdOrNull(1L) } returns transporteBase
        every { transportRepository.delete(transporteBase) } returns Unit
 
        service.deleteTransport(1L)
 
        verify(exactly = 1) { transportRepository.delete(transporteBase) }
    }
 
    @Test
    fun `deleteTransport con id inexistente debe lanzar NotFoundException`() {
        every { transportRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            service.deleteTransport(99L)
        }
    }
}
