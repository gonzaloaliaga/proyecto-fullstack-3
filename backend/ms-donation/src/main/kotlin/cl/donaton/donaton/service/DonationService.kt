package cl.donaton.donaton.service
 
import cl.donaton.donaton.dto.CreateDonationDto
import cl.donaton.donaton.dto.UpdateDonationStatusDto
import cl.donaton.donaton.exception.NotFoundException
import cl.donaton.donaton.model.Donation
import cl.donaton.donaton.model.DonationStatus
import cl.donaton.donaton.repository.DonationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
 
@Service
class DonationService(
    private val donationRepository: DonationRepository
) {
 
    @Transactional
    fun createDonation(dto: CreateDonationDto): Donation {
        val donation = Donation(
            resource = dto.resource,
            quantity = dto.quantity,
            origin = dto.origin,
            donationDate = dto.donationDate,
            collectionCenterId = dto.collectionCenterId,
            status = DonationStatus.PENDING
        )
        return donationRepository.save(donation)
    }
 
    @Transactional(readOnly = true)
    fun getDonationById(id: Long): Donation {
        return donationRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Donación no encontrada")
    }
 
    @Transactional(readOnly = true)
    fun getAllDonations(): List<Donation> {
        return donationRepository.findAll()
    }
 
    @Transactional(readOnly = true)
    fun getDonationsByCollectionCenter(collectionCenterId: Long): List<Donation> {
        return donationRepository.findByCollectionCenterId(collectionCenterId)
    }
 
    @Transactional(readOnly = true)
    fun getDonationsByStatus(status: DonationStatus): List<Donation> {
        return donationRepository.findByStatus(status)
    }
 
    @Transactional
    fun updateDonationStatus(id: Long, dto: UpdateDonationStatusDto): Donation {
        val existingDonation = donationRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Donación no encontrada")
 
        val updatedDonation = existingDonation.copy(status = dto.status)
        return donationRepository.save(updatedDonation)
    }
 
    @Transactional
    fun deleteDonation(id: Long) {
        val existingDonation = donationRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Donación no encontrada")
        donationRepository.delete(existingDonation)
    }
}
