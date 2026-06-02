package cl.donaton.donaton.service

import org.springframework.stereotype.Service
import org.springframework.data.repository.findByIdOrNull

@Service
class ProfileService (
    private val profileRepository: ProfileRepository
) {

    fun getUserProfile(userId: Long): Profile {
        return profileRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("Usuario no encontrado.")
    }

    fun updateUserProfile() {}
}

class UserNotFoundException(message: String) : RuntimeException(message)
