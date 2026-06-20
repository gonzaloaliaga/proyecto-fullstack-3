package cl.donaton.donaton.controller
 
import cl.donaton.donaton.dto.AssignTransportDto
import cl.donaton.donaton.dto.CreateShipmentDto
import cl.donaton.donaton.dto.UpdateShipmentStatusDto
import cl.donaton.donaton.model.Shipment
import cl.donaton.donaton.model.ShipmentStatus
import cl.donaton.donaton.model.Transport
import cl.donaton.donaton.service.ShipmentService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.time.LocalDate
import kotlin.test.assertEquals
 
class ShipmentControllerTest {
 
    private val shipmentService = mockk<ShipmentService>()
    private val controller = ShipmentController(shipmentService)
 
    private val envio = Shipment(
        id = 1L,
        donationId = 1L,
        originCenterId = 1L,
        destination = "Comuna de Til Til",
        status = ShipmentStatus.PLANNED,
        scheduledDate = LocalDate.of(2026, 4, 1)
    )
 
    @Test
    fun `createShipment debe retornar 201 con el envio creado`() {
        val dto = CreateShipmentDto(
            donationId = 1L,
            originCenterId = 1L,
            destination = "Comuna de Til Til",
            scheduledDate = LocalDate.of(2026, 4, 1)
        )
 
        every { shipmentService.createShipment(dto) } returns envio
 
        val response = controller.createShipment(dto)
 
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(envio, response.body)
    }
 
    @Test
    fun `getShipments sin filtros debe retornar todos los envios`() {
        every { shipmentService.getAllShipments() } returns listOf(envio)
 
        val response = controller.getShipments(null, null)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size)
    }
 
    @Test
    fun `getShipments con status debe filtrar por estado`() {
        every { shipmentService.getShipmentsByStatus(ShipmentStatus.PLANNED) } returns listOf(envio)
 
        val response = controller.getShipments(ShipmentStatus.PLANNED, null)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        verify(exactly = 1) { shipmentService.getShipmentsByStatus(ShipmentStatus.PLANNED) }
    }
 
    @Test
    fun `getShipments con originCenterId debe filtrar por centro de origen`() {
        every { shipmentService.getShipmentsByOriginCenter(1L) } returns listOf(envio)
 
        val response = controller.getShipments(null, 1L)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        verify(exactly = 1) { shipmentService.getShipmentsByOriginCenter(1L) }
    }
 
    @Test
    fun `getShipmentById debe retornar 200 con el envio`() {
        every { shipmentService.getShipmentById(1L) } returns envio
 
        val response = controller.getShipmentById(1L)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(envio, response.body)
    }
 
    @Test
    fun `assignTransport debe retornar 200 con el transporte asignado`() {
        val dto = AssignTransportDto(transportId = 1L)
        val transporte = Transport(1L, "ABCD12", "Camión", "Pedro González", 1000, false)
        val actualizado = envio.copy(transport = transporte)
 
        every { shipmentService.assignTransport(1L, dto) } returns actualizado
 
        val response = controller.assignTransport(1L, dto)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(transporte, response.body?.transport)
    }
 
    @Test
    fun `updateShipmentStatus debe retornar 200 con el estado actualizado`() {
        val dto = UpdateShipmentStatusDto(status = ShipmentStatus.IN_TRANSIT)
        val actualizado = envio.copy(status = ShipmentStatus.IN_TRANSIT)
 
        every { shipmentService.updateShipmentStatus(1L, dto) } returns actualizado
 
        val response = controller.updateShipmentStatus(1L, dto)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(ShipmentStatus.IN_TRANSIT, response.body?.status)
    }
 
    @Test
    fun `deleteShipment debe retornar 204`() {
        every { shipmentService.deleteShipment(1L) } returns Unit
 
        val response = controller.deleteShipment(1L)
 
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(exactly = 1) { shipmentService.deleteShipment(1L) }
    }
}
