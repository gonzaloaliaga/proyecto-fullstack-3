package cl.donaton.donaton.service

import cl.donaton.donaton.client.AuthClient
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class BffAuthService(private val authClient: AuthClient) {

    fun login(credentials: ObjectNode): ResponseEntity<JsonNode> {
        return authClient.forwardLogin(credentials)
    }

    fun updateUsername(authHeader: String?, body: ObjectNode): ResponseEntity<JsonNode> {
        return authClient.forwardUpdateUsername(authHeader, body)
    }
}