package cl.donaton.donaton.service
 
import cl.donaton.donaton.dto.AdjustStockDto
import cl.donaton.donaton.dto.CreateInventoryItemDto
import cl.donaton.donaton.exception.InsufficientStockException
import cl.donaton.donaton.exception.NotFoundException
import cl.donaton.donaton.model.CollectionCenter
import cl.donaton.donaton.model.InventoryItem
import cl.donaton.donaton.repository.CollectionCenterRepository
import cl.donaton.donaton.repository.InventoryItemRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.assertEquals
 
class InventoryItemServiceTest {
 
    private val inventoryItemRepository = mockk<InventoryItemRepository>()
    private val collectionCenterRepository = mockk<CollectionCenterRepository>()
    private val service = InventoryItemService(inventoryItemRepository, collectionCenterRepository)
 
    private val centro = CollectionCenter(
        id = 1L,
        name = "Centro de Acopio Central",
        address = "Av. Libertador 1234",
        region = "Metropolitana",
        capacity = 5000,
        active = true
    )
 
    private val itemBase = InventoryItem(
        id = 1L,
        collectionCenter = centro,
        resource = "Ropa de invierno",
        quantity = 50,
        unit = "unidades"
    )
 
    // ── CREATE ─────────────────────────────────────────────────────────────
 
    @Test
    fun `createInventoryItem debe guardar el item cuando el centro existe`() {
        val dto = CreateInventoryItemDto(
            collectionCenterId = 1L,
            resource = "Ropa de invierno",
            quantity = 50,
            unit = "unidades"
        )
 
        every { collectionCenterRepository.findByIdOrNull(1L) } returns centro
        every { inventoryItemRepository.save(any()) } returns itemBase
 
        val result = service.createInventoryItem(dto)
 
        assertEquals("Ropa de invierno", result.resource)
        assertEquals(50, result.quantity)
    }
 
    @Test
    fun `createInventoryItem con centro inexistente debe lanzar NotFoundException`() {
        val dto = CreateInventoryItemDto(
            collectionCenterId = 99L,
            resource = "Ropa de invierno",
            quantity = 50,
            unit = "unidades"
        )
 
        every { collectionCenterRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            service.createInventoryItem(dto)
        }
    }
 
    // ── GET ────────────────────────────────────────────────────────────────
 
    @Test
    fun `getInventoryItemById existente debe retornar el item`() {
        every { inventoryItemRepository.findByIdOrNull(1L) } returns itemBase
 
        val result = service.getInventoryItemById(1L)
 
        assertEquals(itemBase, result)
    }
 
    @Test
    fun `getInventoryItemById inexistente debe lanzar NotFoundException`() {
        every { inventoryItemRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            service.getInventoryItemById(99L)
        }
    }
 
    @Test
    fun `getAllInventoryItems debe retornar todos los items`() {
        every { inventoryItemRepository.findAll() } returns listOf(itemBase)
 
        val result = service.getAllInventoryItems()
 
        assertEquals(1, result.size)
    }
 
    @Test
    fun `getInventoryItemsByCollectionCenter debe filtrar por centro`() {
        every { inventoryItemRepository.findByCollectionCenterId(1L) } returns listOf(itemBase)
 
        val result = service.getInventoryItemsByCollectionCenter(1L)
 
        assertEquals(1, result.size)
        verify(exactly = 1) { inventoryItemRepository.findByCollectionCenterId(1L) }
    }
 
    // ── ADJUST STOCK ───────────────────────────────────────────────────────
 
    @Test
    fun `adjustStock con cantidad positiva debe sumar al stock`() {
        val dto = AdjustStockDto(quantityChange = 20)
        val actualizado = itemBase.copy(quantity = 70)
 
        every { inventoryItemRepository.findByIdOrNull(1L) } returns itemBase
        every { inventoryItemRepository.save(any()) } returns actualizado
 
        val result = service.adjustStock(1L, dto)
 
        assertEquals(70, result.quantity)
    }
 
    @Test
    fun `adjustStock con cantidad negativa valida debe restar del stock`() {
        val dto = AdjustStockDto(quantityChange = -30)
        val actualizado = itemBase.copy(quantity = 20)
 
        every { inventoryItemRepository.findByIdOrNull(1L) } returns itemBase
        every { inventoryItemRepository.save(any()) } returns actualizado
 
        val result = service.adjustStock(1L, dto)
 
        assertEquals(20, result.quantity)
    }
 
    @Test
    fun `adjustStock que dejaria el stock en negativo debe lanzar InsufficientStockException`() {
        val dto = AdjustStockDto(quantityChange = -100)
 
        every { inventoryItemRepository.findByIdOrNull(1L) } returns itemBase
 
        assertThrows<InsufficientStockException> {
            service.adjustStock(1L, dto)
        }
    }
 
    @Test
    fun `adjustStock que deja el stock exactamente en cero debe permitirse`() {
        val dto = AdjustStockDto(quantityChange = -50)
        val actualizado = itemBase.copy(quantity = 0)
 
        every { inventoryItemRepository.findByIdOrNull(1L) } returns itemBase
        every { inventoryItemRepository.save(any()) } returns actualizado
 
        val result = service.adjustStock(1L, dto)
 
        assertEquals(0, result.quantity)
    }
 
    @Test
    fun `adjustStock con item inexistente debe lanzar NotFoundException`() {
        every { inventoryItemRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            service.adjustStock(99L, AdjustStockDto(quantityChange = 10))
        }
    }
 
    // ── DELETE ─────────────────────────────────────────────────────────────
 
    @Test
    fun `deleteInventoryItem existente debe eliminar el item`() {
        every { inventoryItemRepository.findByIdOrNull(1L) } returns itemBase
        every { inventoryItemRepository.delete(itemBase) } returns Unit
 
        service.deleteInventoryItem(1L)
 
        verify(exactly = 1) { inventoryItemRepository.delete(itemBase) }
    }
 
    @Test
    fun `deleteInventoryItem con id inexistente debe lanzar NotFoundException`() {
        every { inventoryItemRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            service.deleteInventoryItem(99L)
        }
    }
}
