package cl.donaton.donaton.service

import cl.donaton.donaton.client.InventoryClient
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class BffInventoryService(private val inventoryClient: InventoryClient) {

    fun getCollectionCenters(authHeader: String?): ResponseEntity<String> =
        inventoryClient.getCollectionCenters(authHeader)

    fun getInventoryItems(authHeader: String?): ResponseEntity<String> =
        inventoryClient.getInventoryItems(authHeader)
}
