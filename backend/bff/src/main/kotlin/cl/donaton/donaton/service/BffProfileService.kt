package cl.donaton.donaton.service

import cl.donaton.donaton.client.ProfileClient
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class BffProfileService(private val profileClient: ProfileClient) {

    fun getUserProfile(userId: Long, authHeader: String?): ResponseEntity<String> {
        return profileClient.fetchUserProfile(userId, authHeader)
    }

    fun updateUserProfile(userId: Long, authHeader: String?, body: String): ResponseEntity<String> {
        return profileClient.forwardUpdateProfile(userId, authHeader, body)
    }
}