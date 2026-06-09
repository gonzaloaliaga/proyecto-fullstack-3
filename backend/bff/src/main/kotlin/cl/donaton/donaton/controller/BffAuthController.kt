package cl.donaton.donaton.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/api/auth")
class BffAuthController {

    private val restTemplate = RestTemplate()
    private val objectMapper = ObjectMapper()

    @Value("\${services.auth.url}")
    lateinit var authServiceUrl: String

    @PostMapping("/login")
    fun login(@RequestBody credentials: Map<String, String>): ResponseEntity<Any> {
        val targetUrl = "$authServiceUrl/api/auth/login"
        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        val request = HttpEntity(credentials, headers)

        return try {
            val response = restTemplate.exchange(targetUrl, HttpMethod.POST, request, Any::class.java)
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

    @PostMapping("/update-username")
    fun updateUsername(
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @RequestBody request: Map<String, String>
    ): ResponseEntity<Any> {
        val targetUrl = "$authServiceUrl/api/auth/update-username"
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            if (!authHeader.isNullOrBlank()) {
                set("Authorization", authHeader)
            }
        }
        val httpRequest = HttpEntity(request, headers)

        return try {
            val response = restTemplate.exchange(targetUrl, HttpMethod.POST, httpRequest, Any::class.java)
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
}