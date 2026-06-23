package cl.donaton.donaton.controller
 
import cl.donaton.donaton.dto.CreateCollectionCenterDto
import cl.donaton.donaton.dto.UpdateCollectionCenterDto
import cl.donaton.donaton.model.CollectionCenter
import cl.donaton.donaton.service.CollectionCenterService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals
 
class CollectionCenterControllerTest {
 
    private val collectionCenterService = mockk<CollectionCenterService>()
    private val controller = CollectionCenterController(collectionCenterService)
 
    private val centro = CollectionCenter(
        id = 1L,
        name = "Centro de Acopio Central",
        address = "Av. Libertador 1234",
        region = "Metropolitana",
        capacity = 5000,
        active = true
    )
 
    @Test
    fun `createCollectionCenter debe retornar 201 con el centro creado`() {
        val dto = CreateCollectionCenterDto(
            name = "Centro de Acopio Central",
            address = "Av. Libertador 1234",
            region = "Metropolitana",
            capacity = 5000
        )
 
        every { collectionCenterService.createCollectionCenter(dto) } returns centro
 
        val response = controller.createCollectionCenter(dto)
 
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(centro, response.body)
    }
 
    @Test
    fun `getCollectionCenters sin filtros debe retornar todos`() {
        every { collectionCenterService.getAllCollectionCenters(false) } returns listOf(centro)
 
        val response = controller.getCollectionCenters(onlyActive = false, region = null)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size)
    }
 
    @Test
    fun `getCollectionCenters con region debe filtrar por region`() {
        every { collectionCenterService.getCollectionCentersByRegion("Metropolitana") } returns listOf(centro)
 
        val response = controller.getCollectionCenters(onlyActive = false, region = "Metropolitana")
 
        assertEquals(HttpStatus.OK, response.statusCode)
        verify(exactly = 1) { collectionCenterService.getCollectionCentersByRegion("Metropolitana") }
    }
 
    @Test
    fun `getCollectionCenterById debe retornar 200 con el centro`() {
        every { collectionCenterService.getCollectionCenterById(1L) } returns centro
 
        val response = controller.getCollectionCenterById(1L)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(centro, response.body)
    }
 
    @Test
    fun `updateCollectionCenter debe retornar 200 con el centro actualizado`() {
        val dto = UpdateCollectionCenterDto(capacity = 8000)
        val actualizado = centro.copy(capacity = 8000)
 
        every { collectionCenterService.updateCollectionCenter(1L, dto) } returns actualizado
 
        val response = controller.updateCollectionCenter(1L, dto)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(8000, response.body?.capacity)
    }
 
    @Test
    fun `deleteCollectionCenter debe retornar 204`() {
        every { collectionCenterService.deleteCollectionCenter(1L) } returns Unit
 
        val response = controller.deleteCollectionCenter(1L)
 
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(exactly = 1) { collectionCenterService.deleteCollectionCenter(1L) }
    }
}
