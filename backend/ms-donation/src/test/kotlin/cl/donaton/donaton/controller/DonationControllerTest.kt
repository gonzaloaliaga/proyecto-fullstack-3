package cl.donaton.donaton.controller
 
import cl.donaton.donaton.dto.CreateDonationDto
import cl.donaton.donaton.dto.UpdateDonationStatusDto
import cl.donaton.donaton.model.Donation
import cl.donaton.donaton.model.DonationStatus
import cl.donaton.donaton.service.DonationService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.time.LocalDate
import kotlin.test.assertEquals
 
class DonationControllerTest {
 
    private val donationService = mockk<DonationService>()
    private val controller = DonationController(donationService)
 
    private val donacion = Donation(
        id = 1L,
        resource = "Ropa de invierno",
        quantity = 50,
        origin = "Empresa TextilSur SpA",
        donationDate = LocalDate.of(2026, 3, 10),
        collectionCenterId = 1L,
        status = DonationStatus.PENDING
    )
 
    @Test
    fun `createDonation debe retornar 201 con la donacion creada`() {
        val dto = CreateDonationDto(
            resource = "Ropa de invierno",
            quantity = 50,
            origin = "Empresa TextilSur SpA",
            donationDate = LocalDate.of(2026, 3, 10),
            collectionCenterId = 1L
        )
 
        every { donationService.createDonation(dto) } returns donacion
 
        val response = controller.createDonation(dto)
 
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(donacion, response.body)
    }
 
    @Test
    fun `getDonations sin filtros debe retornar todas las donaciones`() {
        every { donationService.getAllDonations() } returns listOf(donacion)
 
        val response = controller.getDonations(null, null)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size)
        verify(exactly = 1) { donationService.getAllDonations() }
    }
 
    @Test
    fun `getDonations con collectionCenterId debe filtrar por centro`() {
        every { donationService.getDonationsByCollectionCenter(1L) } returns listOf(donacion)
 
        val response = controller.getDonations(1L, null)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        verify(exactly = 1) { donationService.getDonationsByCollectionCenter(1L) }
    }
 
    @Test
    fun `getDonations con status debe filtrar por estado`() {
        every { donationService.getDonationsByStatus(DonationStatus.PENDING) } returns listOf(donacion)
 
        val response = controller.getDonations(null, DonationStatus.PENDING)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        verify(exactly = 1) { donationService.getDonationsByStatus(DonationStatus.PENDING) }
    }
 
    @Test
    fun `getDonationById debe retornar 200 con la donacion`() {
        every { donationService.getDonationById(1L) } returns donacion
 
        val response = controller.getDonationById(1L)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(donacion, response.body)
    }
 
    @Test
    fun `updateDonationStatus debe retornar 200 con el estado actualizado`() {
        val dto = UpdateDonationStatusDto(status = DonationStatus.RECEIVED)
        val actualizada = donacion.copy(status = DonationStatus.RECEIVED)
 
        every { donationService.updateDonationStatus(1L, dto) } returns actualizada
 
        val response = controller.updateDonationStatus(1L, dto)
 
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(DonationStatus.RECEIVED, response.body?.status)
    }
 
    @Test
    fun `deleteDonation debe retornar 204`() {
        every { donationService.deleteDonation(1L) } returns Unit
 
        val response = controller.deleteDonation(1L)
 
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(exactly = 1) { donationService.deleteDonation(1L) }
    }
}
