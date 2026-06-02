package cl.donaton.donaton.service

import cl.donaton.donaton.dto.UpdateProfileDto
import org.springframework.stereotype.Service
import org.springframework.data.repository.findByIdOrNull

@Service
class ProfileService (
    private val profileRepository: ProfileRepository
) {

    @Transactional(readOnly = true)
    fun getUserProfile(userId: Long): Profile {
        return profileRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("Usuario no encontrado")
    }

    @Transactional
    fun updateUserProfile(userId: Long, dto: UpdateProfileDto) {
        /* 1. Buscar el perfil existente o lanzar excepción si no se encuentra */
        val existingProfile = profileRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("Usuario no encontrado")

        /* 2. Fusionar datos: Si el DTO trae un valor, se usa; si es null, se mantiene el actual */
        val updatedProfile = existingProfile.copy(
            role = dto.role ?: existingProfile.role,
            email = dto.email ?: existingProfile.email,
            address = dto.address ?: existingProfile.address,
            run = dto.run ?: existingProfile.run
        )

        /* 3. Guardar y retornar el perfil actualizado */
        return profileRepository.save(updatedProfile)
    }
}

class UserNotFoundException(message: String) : RuntimeException(message)
