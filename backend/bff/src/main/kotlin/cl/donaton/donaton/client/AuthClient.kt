package cl.donaton.donaton.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate

@Component
class AuthClient(
    @Value("\${services.auth.url}") private val authServiceUrl: String
) {
    
    /* Logs */
    private val logger = LoggerFactory.getLogger(javaClass)

    /* Requests */
    private val restTemplate = RestTemplate()

    fun forwardLogin(body: String): ResponseEntity<String> {
        val targetUrl = "$authServiceUrl/api/auth/login"
        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        val request = HttpEntity(body, headers)

        return executeRequest(targetUrl, HttpMethod.POST, request)
    }

    fun forwardUpdateUsername(authHeader: String?, body: String): ResponseEntity<String> {
        val targetUrl = "$authServiceUrl/api/auth/update-username"
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            if (!authHeader.isNullOrBlank()) {
                set("Authorization", authHeader)
            }
        }
        val request = HttpEntity(body, headers)

        return executeRequest(targetUrl, HttpMethod.POST, request)
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
            logger.error(ex.message)
            
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body("{\"message\": \"Fallo de comunicación en BFF: ${ex.message}\"}")
        }
    }
}