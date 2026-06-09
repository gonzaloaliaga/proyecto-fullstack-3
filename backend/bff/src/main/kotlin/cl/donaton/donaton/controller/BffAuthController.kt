package cl.donaton.donaton.controller

import cl.donaton.donaton.service.BffAuthService
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class BffAuthController(private val bffAuthService: BffAuthService) {

    @PostMapping("/login")
    fun login(@RequestBody credentials: ObjectNode): ResponseEntity<JsonNode> {
        return bffAuthService.login(credentials)
    }

    @PostMapping("/update-username")
    fun updateUsername(
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @RequestAttribute("authenticatedUserId", required = false) authenticatedUserId: String?, // Extraído del Filter automáticamente
        @RequestBody requestBody: ObjectNode
    ): ResponseEntity<JsonNode> {
        return bffAuthService.updateUsername(authHeader, requestBody)
    }
}