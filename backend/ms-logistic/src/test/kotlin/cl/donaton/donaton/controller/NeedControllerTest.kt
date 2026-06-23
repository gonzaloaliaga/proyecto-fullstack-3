package cl.donaton.donaton.controller
 
import cl.donaton.donaton.dto.CreateNeedDto
import cl.donaton.donaton.dto.UpdateNeedStatusDto
import cl.donaton.donaton.model.Need
import cl.donaton.donaton.model.NeedStatus
import cl.donaton.donaton.service.NeedService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals
 
class NeedControllerTest {
 
    private val needService = mockk<NeedService>()
    private val controller = NeedController(needService)
 
    private val necesidad = Need(
        id = 1L,
        resource = "Agua potable",
        quantity = 500,
        location = "Comuna de Til Til",
        reportedBy = "Municipalidad de Til Til",
        status = NeedStatus.REPORTED
    )
 
    @Test
    fun `createNeed debe retornar 201 con la necesidad creada`() {
        val dto = CreateNeedDto(
            resource = "Agua potable",
            quantity = 500,
            location = "Comuna de Til Til",
            reportedBy = "Municipalidad de Til Til"
        )
 
        every { needService.createNeed(dto) } returns necesidad
 
        val response = controller.createNeed(dto)
 
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(necesidad, response.body)
    }
 
    @Test
    fun `getNeeds sin filtros debe retornar todas las necesidades`() {
        every { needService.getAllNeeds() } returns listOf(necesidad)
 
        val response = controller.getNeeds(null, null)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size)
    }
 
    @Test
    fun `getNeeds con status debe filtrar por estado`() {
        every { needService.getNeedsByStatus(NeedStatus.REPORTED) } returns listOf(necesidad)
 
        val response = controller.getNeeds(NeedStatus.REPORTED, null)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        verify(exactly = 1) { needService.getNeedsByStatus(NeedStatus.REPORTED) }
    }
 
    @Test
    fun `getNeeds con location debe filtrar por ubicacion`() {
        every { needService.getNeedsByLocation("Comuna de Til Til") } returns listOf(necesidad)
 
        val response = controller.getNeeds(null, "Comuna de Til Til")
 
        assertEquals(HttpStatus.OK, response.statusCode)
        verify(exactly = 1) { needService.getNeedsByLocation("Comuna de Til Til") }
    }
 
    @Test
    fun `getNeedById debe retornar 200 con la necesidad`() {
        every { needService.getNeedById(1L) } returns necesidad
 
        val response = controller.getNeedById(1L)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(necesidad, response.body)
    }
 
    @Test
    fun `updateNeedStatus debe retornar 200 con el estado actualizado`() {
        val dto = UpdateNeedStatusDto(status = NeedStatus.IN_PROGRESS)
        val actualizada = necesidad.copy(status = NeedStatus.IN_PROGRESS)
 
        every { needService.updateNeedStatus(1L, dto) } returns actualizada
 
        val response = controller.updateNeedStatus(1L, dto)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(NeedStatus.IN_PROGRESS, response.body?.status)
    }
 
    @Test
    fun `deleteNeed debe retornar 204`() {
        every { needService.deleteNeed(1L) } returns Unit
 
        val response = controller.deleteNeed(1L)
 
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(exactly = 1) { needService.deleteNeed(1L) }
    }
}
