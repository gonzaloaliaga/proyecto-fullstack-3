package cl.donaton.donaton.controller

import cl.donaton.donaton.service.BffDonationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/donations")
class BffDonationController(private val bffDonationService: BffDonationService) {

    @GetMapping
    fun getAllDonations(
        @RequestHeader("Authorization", required = false) authHeader: String?
    ): ResponseEntity<String> =
        bffDonationService.getAllDonations(authHeader)

    @PostMapping
    fun createDonation(
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @RequestBody body: String
    ): ResponseEntity<String> =
        bffDonationService.createDonation(authHeader, body)

    @PatchMapping("/{id}/status")
    fun updateDonationStatus(
        @PathVariable id: Long,
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @RequestBody body: String
    ): ResponseEntity<String> =
        bffDonationService.updateDonationStatus(id, authHeader, body)
}
