package cl.donaton.donaton.service
 
import cl.donaton.donaton.dto.CreateNeedDto
import cl.donaton.donaton.dto.UpdateNeedStatusDto
import cl.donaton.donaton.exception.NotFoundException
import cl.donaton.donaton.model.Need
import cl.donaton.donaton.model.NeedStatus
import cl.donaton.donaton.repository.NeedRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.assertEquals
 
class NeedServiceTest {
 
    private val needRepository = mockk<NeedRepository>()
    private val service = NeedService(needRepository)
 
    private val necesidadBase = Need(
        id = 1L,
        resource = "Agua potable",
        quantity = 500,
        location = "Comuna de Til Til",
        latitude = -33.0833,
        longitude = -70.9333,
        reportedBy = "Municipalidad de Til Til",
        status = NeedStatus.REPORTED
    )
 
    // ── CREATE ─────────────────────────────────────────────────────────────
 
    @Test
    fun `createNeed debe guardar y retornar la necesidad con estado REPORTED`() {
        val dto = CreateNeedDto(
            resource = "Agua potable",
            quantity = 500,
            location = "Comuna de Til Til",
            latitude = -33.0833,
            longitude = -70.9333,
            reportedBy = "Municipalidad de Til Til"
        )
 
        every { needRepository.save(any()) } returns necesidadBase
 
        val result = service.createNeed(dto)
 
        assertEquals(NeedStatus.REPORTED, result.status)
        assertEquals("Agua potable", result.resource)
        verify(exactly = 1) { needRepository.save(any()) }
    }
 
    // ── GET ────────────────────────────────────────────────────────────────
 
    @Test
    fun `getNeedById existente debe retornar la necesidad`() {
        every { needRepository.findByIdOrNull(1L) } returns necesidadBase
 
        val result = service.getNeedById(1L)
 
        assertEquals(necesidadBase, result)
    }
 
    @Test
    fun `getNeedById inexistente debe lanzar NotFoundException`() {
        every { needRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            service.getNeedById(99L)
        }
    }
 
    @Test
    fun `getAllNeeds debe retornar todas las necesidades`() {
        every { needRepository.findAll() } returns listOf(necesidadBase)
 
        val result = service.getAllNeeds()
 
        assertEquals(1, result.size)
    }
 
    @Test
    fun `getNeedsByStatus debe filtrar por estado`() {
        every { needRepository.findByStatus(NeedStatus.REPORTED) } returns listOf(necesidadBase)
 
        val result = service.getNeedsByStatus(NeedStatus.REPORTED)
 
        assertEquals(1, result.size)
        assertEquals(NeedStatus.REPORTED, result.first().status)
    }
 
    @Test
    fun `getNeedsByLocation debe filtrar por ubicacion`() {
        every { needRepository.findByLocation("Comuna de Til Til") } returns listOf(necesidadBase)
 
        val result = service.getNeedsByLocation("Comuna de Til Til")
 
        assertEquals(1, result.size)
    }
 
    // ── UPDATE STATUS ──────────────────────────────────────────────────────
 
    @Test
    fun `updateNeedStatus debe actualizar el estado correctamente`() {
        val dto = UpdateNeedStatusDto(status = NeedStatus.IN_PROGRESS)
        val actualizada = necesidadBase.copy(status = NeedStatus.IN_PROGRESS)
 
        every { needRepository.findByIdOrNull(1L) } returns necesidadBase
        every { needRepository.save(actualizada) } returns actualizada
 
        val result = service.updateNeedStatus(1L, dto)
 
        assertEquals(NeedStatus.IN_PROGRESS, result.status)
    }
 
    @Test
    fun `updateNeedStatus con id inexistente debe lanzar NotFoundException`() {
        every { needRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            service.updateNeedStatus(99L, UpdateNeedStatusDto(NeedStatus.IN_PROGRESS))
        }
    }
 
    // ── DELETE ─────────────────────────────────────────────────────────────
 
    @Test
    fun `deleteNeed existente debe eliminar la necesidad`() {
        every { needRepository.findByIdOrNull(1L) } returns necesidadBase
        every { needRepository.delete(necesidadBase) } returns Unit
 
        service.deleteNeed(1L)
 
        verify(exactly = 1) { needRepository.delete(necesidadBase) }
    }
 
    @Test
    fun `deleteNeed con id inexistente debe lanzar NotFoundException`() {
        every { needRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            service.deleteNeed(99L)
        }
    }
}
