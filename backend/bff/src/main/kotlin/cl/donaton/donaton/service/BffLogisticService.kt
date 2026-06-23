package cl.donaton.donaton.service

import cl.donaton.donaton.client.LogisticClient
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class BffLogisticService(private val logisticClient: LogisticClient) {

    fun getNeeds(authHeader: String?): ResponseEntity<String> =
        logisticClient.getNeeds(authHeader)

    fun getShipments(authHeader: String?): ResponseEntity<String> =
        logisticClient.getShipments(authHeader)
}
