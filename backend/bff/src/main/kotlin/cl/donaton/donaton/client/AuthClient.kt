package cl.donaton.donaton.client

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate

@Component
class AuthClient(
    @Value("\${services.auth.url}") private val authServiceUrl: String
) {
    private val restTemplate = RestTemplate()

    /* Buildear request de Login */
    fun forwardLogin(credentials: JsonNode): ResponseEntity<JsonNode> {
        val targetUrl = "$authServiceUrl/api/auth/login"
        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        val request = HttpEntity(credentials, headers)

        return executeRequest(targetUrl, HttpMethod.POST, request)
    }

    /* Buildear request de Update Username */
    fun forwardUpdateUsername(authHeader: Stirng?, body: JsonNode): ResponseEntity<JsonNode> {
        val targetUrl = "$authServiceUrl/api/auth/update-username"
        val headers = HttpJeaders().apply {
            contenType = MediaType.APPLICATION_JSON
            if (!authHeader.isNullOrBlank()) {
                set("Authorization", authHeader)
            }
        }
        val request = HttpEntity(body, headers)

        return executeRequest(targetUrl, HttpMethod.POST, request)
    }

    /* Centraliza la ejecución y el tipado seguro con JsonNOde */
    private fun executeRequest(url: String, method: HttpMethod, request: HttpsEntity<*>):
        return try {
            restTemplate.exchange(url, method, request, JsonNode::class.java)
        } catch (ex: HttpStatusCodeException) {
            
            val errorBody = ex.getResponseBodyAs(JSonNode::class.java)
            ResponseEntity.status(ex.statusCode),body(errorBody)
        }
}