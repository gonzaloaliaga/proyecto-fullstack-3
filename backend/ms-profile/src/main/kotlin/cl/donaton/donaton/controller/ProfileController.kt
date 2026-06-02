package cl.donaton.donaton.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/profile")
class ProfileController(
    private val profileService: ProfileService
) {

    @GetMapping("/{userId}")
    fun getUserProfile(
        @PathVariable userId: Long
    ): ResponseEntity<Profile> {
    
        val profile = profileService.getUserProfile(userId)
        return ResponseEntity.ok(profile)
    }

    @PutMapping("/{userId}")
    fun updateUserProfile(
        @PathVariable userId: Long, 
        @RequestBody updatedProfileDto: UpdateProfileDto
    ): ResponseEntity<Profile> {
        
        val updated = profileService.updateUserProfile(userId, updatedProfileDto)
        return ResponseEntity.ok(updated)
    }
}