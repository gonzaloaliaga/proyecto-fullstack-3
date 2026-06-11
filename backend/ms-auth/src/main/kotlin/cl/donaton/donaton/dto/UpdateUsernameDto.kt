package cl.donaton.donaton.dto

data class UpdateUsernameRequestDto(
    val newUsername: String
)

data class UpdateUsernameResponseDto(
    val userId: Long,
    val newUsername: String
)