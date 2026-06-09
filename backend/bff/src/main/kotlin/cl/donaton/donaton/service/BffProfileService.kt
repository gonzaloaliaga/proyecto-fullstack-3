package cl.donaton.donaton.service

import cl.donaton.donaton.client.ProfileClient
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class BffProfileService(private val profileClient: ProfileClient) {

    fun getUserProfile(userId: Long, authHeader: String?): ResponseEntity<JsonNode> {
        return profileClient.fetchUserProfile(userId, authHeader)
    }

    fun updateUserProfile(userId: Long, authHeader: String?, body: ObjectNode): ResponseEntity<JsonNode> {
        return profileClient.forwardUpdateProfile(userId, authHeader, body)
    }
}