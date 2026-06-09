package cl.donaton.donaton.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/api/profile")
class BffProfileController(
    @Value("\${services.profile.url}") private val profileServiceUrl: String
) {

    private val restTemplate = RestTemplate(HttpComponentsClientHttpRequestFactory(HttpClients.createDefault()))
    private val objectMapper = ObjectMapper()

    @GetMapping("/{userId}")
    fun getUserProfile(
        @PathVariable userId: Long,
        @RequestHeader("Authorization", required = false) authHeader: String?
    ): ResponseEntity<Any> {
        val targetUrl = "$profileServiceUrl/api/profile/$userId"
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            if (!authHeader.isNullOrBlank()) {
                set("Authorization", authHeader)
            }
        }
        val request = HttpEntity("", headers)

        return try {
            val response = restTemplate.exchange(targetUrl, HttpMethod.GET, request, Any::class.java)
            ResponseEntity.status(response.statusCode).body(response.body)
        } catch (ex: HttpStatusCodeException) {
            val errorBody = try {
                objectMapper.readValue(ex.responseBodyAsString, Any::class.java)
            } catch (_: Exception) {
                mapOf("message" to ex.statusText)
            }
            ResponseEntity.status(ex.statusCode).body(errorBody)
        }
    }

    @PatchMapping("/{userId}")
    fun updateUserProfile(
        @PathVariable userId: Long,
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @RequestBody updatedProfile: Map<String, String?>
    ): ResponseEntity<Any> {
        val targetUrl = "$profileServiceUrl/api/profile/$userId"
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            if (!authHeader.isNullOrBlank()) {
                set("Authorization", authHeader)
            }
        }
        val request = HttpEntity(updatedProfile, headers)

        return try {
            val response = restTemplate.exchange(targetUrl, HttpMethod.PATCH, request, Any::class.java)
            ResponseEntity.status(response.statusCode).body(response.body)
        } catch (ex: HttpStatusCodeException) {
            val errorBody = try {
                objectMapper.readValue(ex.responseBodyAsString, Any::class.java)
            } catch (_: Exception) {
                mapOf("message" to "Error del Profile Service: ${ex.statusCode}")
            }
            ResponseEntity.status(ex.statusCode).body(errorBody)
        } catch (ex: Exception) {
            // Este catch atrapará errores como "Connection refused" si el microservicio está apagado
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("message" to "Fallo de comunicación en BFF: ${ex.message}"))
        }
    }
}