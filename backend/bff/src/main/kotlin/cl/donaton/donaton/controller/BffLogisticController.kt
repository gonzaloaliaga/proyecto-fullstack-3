package cl.donaton.donaton.controller

import cl.donaton.donaton.service.BffLogisticService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class BffLogisticController(private val bffLogisticService: BffLogisticService) {

    @GetMapping("/needs")
    fun getNeeds(
        @RequestHeader("Authorization", required = false) authHeader: String?
    ): ResponseEntity<String> =
        bffLogisticService.getNeeds(authHeader)

    @GetMapping("/shipments")
    fun getShipments(
        @RequestHeader("Authorization", required = false) authHeader: String?
    ): ResponseEntity<String> =
        bffLogisticService.getShipments(authHeader)
}
