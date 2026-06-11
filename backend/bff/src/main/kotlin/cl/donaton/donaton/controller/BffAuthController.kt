package cl.donaton.donaton.controller

import cl.donaton.donaton.service.BffAuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class BffAuthController(private val bffAuthService: BffAuthService) {

    @PostMapping("/login")
    fun login(@RequestBody body: String): ResponseEntity<String> {
        return bffAuthService.login(body)
    }

    @PostMapping("/update-username")
    fun updateUsername(
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @RequestAttribute("authenticatedUserId", required = false) authenticatedUserId: String?,
        @RequestBody requestBody: String
    ): ResponseEntity<String> {
        return bffAuthService.updateUsername(authHeader, requestBody)
    }
}