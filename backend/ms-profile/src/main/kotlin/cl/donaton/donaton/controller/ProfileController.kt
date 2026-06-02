package cl.donaton.donaton.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/profile")
class ProfileController(
    private val profileService: ProfileService
) {

    @GetMapping("/{userId}")
    fun getUserProfile(@PathVariable userId: Long): ResponseEntity<Profile> {
        val profile = profileService.getUserProfile(userId)
        ResponseEntity.ok(profile)
    }

    @PatchMapping("/{userId}")
    fun updateUserProfile(@PathVariable userId: Long, @RequestBody updatedProfile: Map<String, Any>): ResponseEntity<Any> {
        val existingProfile = profileRepository.findById(userId).orElse(null)
        
        return if (existingProfile != null) {
            val updated = existingProfile.copy(
                role = updatedProfile["role"] as? String ?: existingProfile.role,
                email = updatedProfile["email"] as? String ?: existingProfile.email,
                address = updatedProfile["address"] as? String ?: existingProfile.address,
                run = updatedProfile["run"] as? String ?: existingProfile.run
            )

            profileRepository.save(updated)
            ResponseEntity.ok(updated)
        } else {
            ResponseEntity.status(404).body(mapOf("message" to "Usuario no encontrado"))
        }
    }
}