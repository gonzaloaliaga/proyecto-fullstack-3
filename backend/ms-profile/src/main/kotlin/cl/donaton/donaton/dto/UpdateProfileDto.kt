package cl.donaton.donaton.dto

data class UpdateProfileDto(
    val role: String? = null,
    val email: String? = null,
    val address: String? = null,
    val run: String? = null
)