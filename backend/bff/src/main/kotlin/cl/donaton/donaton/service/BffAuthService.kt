package cl.donaton.donaton.service

import cl.donaton.donaton.client.AuthClient
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class BffAuthService(private val authClient: AuthClient) {

    fun login(body: String): ResponseEntity<String> {
        return authClient.forwardLogin(body)
    }

    fun updateUsername(authHeader: String?, body: String): ResponseEntity<String> {
        return authClient.forwardUpdateUsername(authHeader, body)
    }
}