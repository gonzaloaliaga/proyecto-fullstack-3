package cl.donaton.donaton.service
 
import cl.donaton.donaton.dto.CreateCollectionCenterDto
import cl.donaton.donaton.dto.UpdateCollectionCenterDto
import cl.donaton.donaton.exception.NotFoundException
import cl.donaton.donaton.model.CollectionCenter
import cl.donaton.donaton.repository.CollectionCenterRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.assertEquals
 
class CollectionCenterServiceTest {
 
    private val collectionCenterRepository = mockk<CollectionCenterRepository>()
    private val service = CollectionCenterService(collectionCenterRepository)
 
    private val centroBase = CollectionCenter(
        id = 1L,
        name = "Centro de Acopio Central",
        address = "Av. Libertador 1234, Santiago",
        region = "Metropolitana",
        capacity = 5000,
        active = true
    )
 
    // ── CREATE ─────────────────────────────────────────────────────────────
 
    @Test
    fun `createCollectionCenter debe guardar y retornar el centro como activo`() {
        val dto = CreateCollectionCenterDto(
            name = "Centro de Acopio Central",
            address = "Av. Libertador 1234, Santiago",
            region = "Metropolitana",
            capacity = 5000
        )
 
        every { collectionCenterRepository.save(any()) } returns centroBase
 
        val result = service.createCollectionCenter(dto)
 
        assertEquals(true, result.active)
        assertEquals("Centro de Acopio Central", result.name)
    }
 
    // ── GET BY ID ──────────────────────────────────────────────────────────
 
    @Test
    fun `getCollectionCenterById existente debe retornar el centro`() {
        every { collectionCenterRepository.findByIdOrNull(1L) } returns centroBase
 
        val result = service.getCollectionCenterById(1L)
 
        assertEquals(centroBase, result)
    }
 
    @Test
    fun `getCollectionCenterById inexistente debe lanzar NotFoundException`() {
        every { collectionCenterRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            service.getCollectionCenterById(99L)
        }
    }
 
    // ── LISTADOS ───────────────────────────────────────────────────────────
 
    @Test
    fun `getAllCollectionCenters con onlyActive false debe retornar todos`() {
        every { collectionCenterRepository.findAll() } returns listOf(centroBase)
 
        val result = service.getAllCollectionCenters(onlyActive = false)
 
        assertEquals(1, result.size)
        verify(exactly = 1) { collectionCenterRepository.findAll() }
    }
 
    @Test
    fun `getAllCollectionCenters con onlyActive true debe filtrar solo activos`() {
        every { collectionCenterRepository.findByActiveTrue() } returns listOf(centroBase)
 
        val result = service.getAllCollectionCenters(onlyActive = true)
 
        assertEquals(1, result.size)
        verify(exactly = 1) { collectionCenterRepository.findByActiveTrue() }
    }
 
    @Test
    fun `getCollectionCentersByRegion debe filtrar por region`() {
        every { collectionCenterRepository.findByRegion("Metropolitana") } returns listOf(centroBase)
 
        val result = service.getCollectionCentersByRegion("Metropolitana")
 
        assertEquals(1, result.size)
    }
 
    // ── UPDATE ─────────────────────────────────────────────────────────────
 
    @Test
    fun `updateCollectionCenter debe fusionar solo los campos no nulos del DTO`() {
        val dto = UpdateCollectionCenterDto(capacity = 8000)
        val actualizado = centroBase.copy(capacity = 8000)
 
        every { collectionCenterRepository.findByIdOrNull(1L) } returns centroBase
        every { collectionCenterRepository.save(actualizado) } returns actualizado
 
        val result = service.updateCollectionCenter(1L, dto)
 
        assertEquals(8000, result.capacity)
        assertEquals("Centro de Acopio Central", result.name)
    }
 
    @Test
    fun `updateCollectionCenter puede desactivar un centro`() {
        val dto = UpdateCollectionCenterDto(active = false)
        val actualizado = centroBase.copy(active = false)
 
        every { collectionCenterRepository.findByIdOrNull(1L) } returns centroBase
        every { collectionCenterRepository.save(actualizado) } returns actualizado
 
        val result = service.updateCollectionCenter(1L, dto)
 
        assertEquals(false, result.active)
    }
 
    @Test
    fun `updateCollectionCenter con id inexistente debe lanzar NotFoundException`() {
        every { collectionCenterRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            service.updateCollectionCenter(99L, UpdateCollectionCenterDto(name = "X"))
        }
    }
 
    // ── DELETE ─────────────────────────────────────────────────────────────
 
    @Test
    fun `deleteCollectionCenter existente debe eliminar el centro`() {
        every { collectionCenterRepository.findByIdOrNull(1L) } returns centroBase
        every { collectionCenterRepository.delete(centroBase) } returns Unit
 
        service.deleteCollectionCenter(1L)
 
        verify(exactly = 1) { collectionCenterRepository.delete(centroBase) }
    }
 
    @Test
    fun `deleteCollectionCenter con id inexistente debe lanzar NotFoundException`() {
        every { collectionCenterRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            service.deleteCollectionCenter(99L)
        }
    }
}
