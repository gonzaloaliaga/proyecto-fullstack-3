package cl.donaton.donaton.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate

@Component
class LogisticClient(
    @Value("\${services.logistic.url}") private val logisticServiceUrl: String
) {
    private val restTemplate = RestTemplate()

    fun getNeeds(authHeader: String?): ResponseEntity<String> {
        val url = "$logisticServiceUrl/api/needs"
        return executeRequest(url, HttpMethod.GET, null, authHeader)
    }

    fun getShipments(authHeader: String?): ResponseEntity<String> {
        val url = "$logisticServiceUrl/api/shipments"
        return executeRequest(url, HttpMethod.GET, null, authHeader)
    }

    private fun executeRequest(
        url: String,
        method: HttpMethod,
        body: String?,
        authHeader: String?
    ): ResponseEntity<String> {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            if (!authHeader.isNullOrBlank()) set("Authorization", authHeader)
        }
        val request = HttpEntity(body, headers)
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
