package cl.donaton.donaton.controller
 
import cl.donaton.donaton.dto.AdjustStockDto
import cl.donaton.donaton.dto.CreateInventoryItemDto
import cl.donaton.donaton.model.CollectionCenter
import cl.donaton.donaton.model.InventoryItem
import cl.donaton.donaton.service.InventoryItemService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals
 
class InventoryItemControllerTest {
 
    private val inventoryItemService = mockk<InventoryItemService>()
    private val controller = InventoryItemController(inventoryItemService)
 
    private val centro = CollectionCenter(
        id = 1L,
        name = "Centro de Acopio Central",
        address = "Av. Libertador 1234",
        region = "Metropolitana",
        capacity = 5000,
        active = true
    )
 
    private val item = InventoryItem(
        id = 1L,
        collectionCenter = centro,
        resource = "Ropa de invierno",
        quantity = 50,
        unit = "unidades"
    )
 
    @Test
    fun `createInventoryItem debe retornar 201 con el item creado`() {
        val dto = CreateInventoryItemDto(
            collectionCenterId = 1L,
            resource = "Ropa de invierno",
            quantity = 50,
            unit = "unidades"
        )
 
        every { inventoryItemService.createInventoryItem(dto) } returns item
 
        val response = controller.createInventoryItem(dto)
 
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(item, response.body)
    }
 
    @Test
    fun `getInventoryItems sin filtro debe retornar todos los items`() {
        every { inventoryItemService.getAllInventoryItems() } returns listOf(item)
 
        val response = controller.getInventoryItems(null)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size)
    }
 
    @Test
    fun `getInventoryItems con collectionCenterId debe filtrar por centro`() {
        every { inventoryItemService.getInventoryItemsByCollectionCenter(1L) } returns listOf(item)
 
        val response = controller.getInventoryItems(1L)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        verify(exactly = 1) { inventoryItemService.getInventoryItemsByCollectionCenter(1L) }
    }
 
    @Test
    fun `getInventoryItemById debe retornar 200 con el item`() {
        every { inventoryItemService.getInventoryItemById(1L) } returns item
 
        val response = controller.getInventoryItemById(1L)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(item, response.body)
    }
 
    @Test
    fun `adjustStock debe retornar 200 con el stock actualizado`() {
        val dto = AdjustStockDto(quantityChange = -10)
        val actualizado = item.copy(quantity = 40)
 
        every { inventoryItemService.adjustStock(1L, dto) } returns actualizado
 
        val response = controller.adjustStock(1L, dto)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(40, response.body?.quantity)
    }
 
    @Test
    fun `deleteInventoryItem debe retornar 204`() {
        every { inventoryItemService.deleteInventoryItem(1L) } returns Unit
 
        val response = controller.deleteInventoryItem(1L)
 
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(exactly = 1) { inventoryItemService.deleteInventoryItem(1L) }
    }
}
