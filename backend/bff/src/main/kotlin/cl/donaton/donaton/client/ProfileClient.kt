package cl.donaton.donaton.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate

@Component
class ProfileClient(
    @Value("\${services.profile.url}") private val profileServiceUrl: String
) {
    private val restTemplate = RestTemplate()

    /* Buildear request de GET UserProfile */
    fun fetchUserProfile(userId: Long, authHeader: String?): ResponseEntity<String> {
        val targetUrl = "$profileServiceUrl/api/profile/$userId"
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            if (!authHeader.isNullOrBlank()) {
                set("Authorization", authHeader)
            }
        }
        val request = HttpEntity(null, headers)

        return executeRequest(targetUrl, HttpMethod.GET, request)
    }

    /* Buildear request de PUT UpdateProfile */
    fun forwardUpdateProfile(userId: Long, authHeader: String?, body: String): ResponseEntity<String> {
        val targetUrl = "$profileServiceUrl/api/profile/$userId"
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            if (!authHeader.isNullOrBlank()) {
                set("Authorization", authHeader)
            }
        }
        val request = HttpEntity(body, headers)

        return executeRequest(targetUrl, HttpMethod.PUT, request)
    }

    /* Centraliza la ejecución */
private fun executeRequest(url: String, method: HttpMethod, request: HttpEntity<*>): ResponseEntity<String> {
        return try {
            restTemplate.exchange(url, method, request, String::class.java)
        } catch (ex: HttpStatusCodeException) {
            ResponseEntity.status(ex.statusCode)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(ex.responseBodyAsString)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body("{\"message\": \"Fallo de comunicación en BFF: ${ex.message}\"}")
        }
    }
}