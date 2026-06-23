package cl.donaton.donaton.service

import cl.donaton.donaton.client.DonationClient
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class BffDonationService(private val donationClient: DonationClient) {

    fun getAllDonations(authHeader: String?): ResponseEntity<String> =
        donationClient.getAllDonations(authHeader)

    fun createDonation(authHeader: String?, body: String): ResponseEntity<String> =
        donationClient.createDonation(authHeader, body)

    fun updateDonationStatus(id: Long, authHeader: String?, body: String): ResponseEntity<String> =
        donationClient.updateDonationStatus(id, authHeader, body)
}
