package cl.donaton.donaton.controller
 
import cl.donaton.donaton.dto.CreateTransportDto
import cl.donaton.donaton.model.Transport
import cl.donaton.donaton.service.TransportService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals
 
class TransportControllerTest {
 
    private val transportService = mockk<TransportService>()
    private val controller = TransportController(transportService)
 
    private val transporte = Transport(
        id = 1L,
        vehiclePlate = "ABCD12",
        vehicleType = "Camión",
        driverName = "Pedro González",
        capacity = 1000,
        available = true
    )
 
    @Test
    fun `createTransport debe retornar 201 con el transporte creado`() {
        val dto = CreateTransportDto(
            vehiclePlate = "ABCD12",
            vehicleType = "Camión",
            driverName = "Pedro González",
            capacity = 1000
        )
 
        every { transportService.createTransport(dto) } returns transporte
 
        val response = controller.createTransport(dto)
 
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(transporte, response.body)
    }
 
    @Test
    fun `getTransports sin filtro debe retornar todos los transportes`() {
        every { transportService.getAllTransports(false) } returns listOf(transporte)
 
        val response = controller.getTransports(onlyAvailable = false)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size)
    }
 
    @Test
    fun `getTransports con onlyAvailable true debe filtrar disponibles`() {
        every { transportService.getAllTransports(true) } returns listOf(transporte)
 
        val response = controller.getTransports(onlyAvailable = true)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        verify(exactly = 1) { transportService.getAllTransports(true) }
    }
 
    @Test
    fun `getTransportById debe retornar 200 con el transporte`() {
        every { transportService.getTransportById(1L) } returns transporte
 
        val response = controller.getTransportById(1L)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(transporte, response.body)
    }
 
    @Test
    fun `setTransportAvailability debe retornar 200 con la disponibilidad actualizada`() {
        val actualizado = transporte.copy(available = false)
 
        every { transportService.setTransportAvailability(1L, false) } returns actualizado
 
        val response = controller.setTransportAvailability(1L, false)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(false, response.body?.available)
    }
 
    @Test
    fun `deleteTransport debe retornar 204`() {
        every { transportService.deleteTransport(1L) } returns Unit
 
        val response = controller.deleteTransport(1L)
 
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(exactly = 1) { transportService.deleteTransport(1L) }
    }
}
