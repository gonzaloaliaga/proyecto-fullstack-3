package cl.donaton.donaton.service
 
import cl.donaton.donaton.dto.CreateDonationDto
import cl.donaton.donaton.dto.UpdateDonationStatusDto
import cl.donaton.donaton.exception.NotFoundException
import cl.donaton.donaton.model.Donation
import cl.donaton.donaton.model.DonationStatus
import cl.donaton.donaton.repository.DonationRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDate
import kotlin.test.assertEquals
 
class DonationServiceTest {
 
    private val donationRepository = mockk<DonationRepository>()
    private val donationService = DonationService(donationRepository)
 
    private val donacionBase = Donation(
        id = 1L,
        resource = "Ropa de invierno",
        quantity = 50,
        origin = "Empresa TextilSur SpA",
        donationDate = LocalDate.of(2026, 3, 10),
        collectionCenterId = 1L,
        status = DonationStatus.PENDING
    )
 
    // ── CREATE ─────────────────────────────────────────────────────────────
 
    @Test
    fun `createDonation debe guardar y retornar la donacion con estado PENDING`() {
        val dto = CreateDonationDto(
            resource = "Ropa de invierno",
            quantity = 50,
            origin = "Empresa TextilSur SpA",
            donationDate = LocalDate.of(2026, 3, 10),
            collectionCenterId = 1L
        )
 
        every { donationRepository.save(any()) } returns donacionBase
 
        val result = donationService.createDonation(dto)
 
        assertEquals(DonationStatus.PENDING, result.status)
        assertEquals("Ropa de invierno", result.resource)
        verify(exactly = 1) { donationRepository.save(any()) }
    }
 
    // ── GET BY ID ──────────────────────────────────────────────────────────
 
    @Test
    fun `getDonationById existente debe retornar la donacion`() {
        every { donationRepository.findByIdOrNull(1L) } returns donacionBase
 
        val result = donationService.getDonationById(1L)
 
        assertEquals(donacionBase, result)
    }
 
    @Test
    fun `getDonationById inexistente debe lanzar NotFoundException`() {
        every { donationRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            donationService.getDonationById(99L)
        }
    }
 
    // ── LISTADOS ───────────────────────────────────────────────────────────
 
    @Test
    fun `getAllDonations debe retornar todas las donaciones`() {
        every { donationRepository.findAll() } returns listOf(donacionBase)
 
        val result = donationService.getAllDonations()
 
        assertEquals(1, result.size)
    }
 
    @Test
    fun `getDonationsByCollectionCenter debe filtrar por centro de acopio`() {
        every { donationRepository.findByCollectionCenterId(1L) } returns listOf(donacionBase)
 
        val result = donationService.getDonationsByCollectionCenter(1L)
 
        assertEquals(1, result.size)
        assertEquals(1L, result.first().collectionCenterId)
        verify(exactly = 1) { donationRepository.findByCollectionCenterId(1L) }
    }
 
    @Test
    fun `getDonationsByStatus debe filtrar por estado`() {
        every { donationRepository.findByStatus(DonationStatus.PENDING) } returns listOf(donacionBase)
 
        val result = donationService.getDonationsByStatus(DonationStatus.PENDING)
 
        assertEquals(1, result.size)
        assertEquals(DonationStatus.PENDING, result.first().status)
    }
 
    // ── UPDATE STATUS ──────────────────────────────────────────────────────
 
    @Test
    fun `updateDonationStatus debe actualizar el estado correctamente`() {
        val dto = UpdateDonationStatusDto(status = DonationStatus.RECEIVED)
        val actualizada = donacionBase.copy(status = DonationStatus.RECEIVED)
 
        every { donationRepository.findByIdOrNull(1L) } returns donacionBase
        every { donationRepository.save(actualizada) } returns actualizada
 
        val result = donationService.updateDonationStatus(1L, dto)
 
        assertEquals(DonationStatus.RECEIVED, result.status)
    }
 
    @Test
    fun `updateDonationStatus con id inexistente debe lanzar NotFoundException`() {
        every { donationRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            donationService.updateDonationStatus(99L, UpdateDonationStatusDto(DonationStatus.RECEIVED))
        }
    }
 
    // ── DELETE ─────────────────────────────────────────────────────────────
 
    @Test
    fun `deleteDonation existente debe eliminar la donacion`() {
        every { donationRepository.findByIdOrNull(1L) } returns donacionBase
        every { donationRepository.delete(donacionBase) } returns Unit
 
        donationService.deleteDonation(1L)
 
        verify(exactly = 1) { donationRepository.delete(donacionBase) }
    }
 
    @Test
    fun `deleteDonation con id inexistente debe lanzar NotFoundException`() {
        every { donationRepository.findByIdOrNull(99L) } returns null
 
        assertThrows<NotFoundException> {
            donationService.deleteDonation(99L)
        }
    }
}
