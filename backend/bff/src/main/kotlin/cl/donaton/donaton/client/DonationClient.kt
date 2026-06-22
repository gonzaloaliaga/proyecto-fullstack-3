package cl.donaton.donaton.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate

@Component
class DonationClient(
    @Value("\${services.donations.url}") private val donationServiceUrl: String
) {
    private val restTemplate = RestTemplate()

    fun getAllDonations(authHeader: String?): ResponseEntity<String> {
        val url = "$donationServiceUrl/api/donations"
        return executeRequest(url, HttpMethod.GET, null, authHeader)
    }

    fun createDonation(authHeader: String?, body: String): ResponseEntity<String> {
        val url = "$donationServiceUrl/api/donations"
        return executeRequest(url, HttpMethod.POST, body, authHeader)
    }

    fun updateDonationStatus(id: Long, authHeader: String?, body: String): ResponseEntity<String> {
        val url = "$donationServiceUrl/api/donations/$id/status"
        return executeRequest(url, HttpMethod.PATCH, body, authHeader)
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
