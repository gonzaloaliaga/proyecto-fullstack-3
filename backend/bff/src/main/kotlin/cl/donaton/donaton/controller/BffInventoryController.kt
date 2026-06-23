package cl.donaton.donaton.controller

import cl.donaton.donaton.service.BffInventoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class BffInventoryController(private val bffInventoryService: BffInventoryService) {

    @GetMapping("/collection-centers")
    fun getCollectionCenters(
        @RequestHeader("Authorization", required = false) authHeader: String?
    ): ResponseEntity<String> =
        bffInventoryService.getCollectionCenters(authHeader)

    @GetMapping("/inventory-items")
    fun getInventoryItems(
        @RequestHeader("Authorization", required = false) authHeader: String?
    ): ResponseEntity<String> =
        bffInventoryService.getInventoryItems(authHeader)
}
