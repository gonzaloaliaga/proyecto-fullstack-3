package cl.donaton.donaton.controller

import cl.donaton.donaton.service.BffProfileService
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/profile")
class BffProfileController(private val bffProfileService: BffProfileService) {

    @GetMapping("/{userId}")
    fun getUserProfile(
        @PathVariable userId: Long,
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @RequestAttribute("authenticatedUserId", required = false) authenticatedUserId: String?
    ): ResponseEntity<JsonNode> {

        return bffProfileService.getUserProfile(userId, authHeader)
    }

    @PutMapping("/{userId}")
    fun updateUserProfile(
        @PathVariable userId: Long,
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @RequestAttribute("authenticatedUserId", required = false) authenticatedUserId: String?,
        @RequestBody updatedProfile: ObjectNode
    ): ResponseEntity<JsonNode> {
        
        return bffProfileService.updateUserProfile(userId, authHeader, updatedProfile)
    }
}