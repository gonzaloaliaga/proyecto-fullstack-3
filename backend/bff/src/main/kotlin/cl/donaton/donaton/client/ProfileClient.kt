package cl.donaton.donaton.client

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
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
    private val objectMapper = ObjectMapper()

    /* Buildear request de GET UserProfile */
    fun fetchUserProfile(userId: Long, authHeader: String?): ResponseEntity<JsonNode> {
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
    fun forwardUpdateProfile(userId: Long, authHeader: String?, body: JsonNode): ResponseEntity<JsonNode> {
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

    /* Centraliza la ejecución y el tipado seguro con JsonNOde */
    private fun executeRequest(url: String, method: HttpMethod, request: HttpEntity<*>): ResponseEntity<JsonNode> {
        return try {
            restTemplate.exchange(url, method, request, JsonNode::class.java)
        } catch (ex: HttpStatusCodeException) {
            val errorBody: JsonNode? = try {
                if (!ex.responseBodyAsString.isNullOrBlank()) {
                    objectMapper.readTree(ex.responseBodyAsString)
                } else null
            } catch (_: Exception) {
                null
            }

            return if (errorBody != null) {
                ResponseEntity.status(ex.statusCode).body(errorBody)
            } else {
                ResponseEntity.status(ex.statusCode).build()
            }
        }
    }
}