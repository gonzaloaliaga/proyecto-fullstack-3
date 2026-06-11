package cl.donaton.donaton.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import cl.donaton.donaton.repository.UserRepository
import cl.donaton.donaton.factory.AuthResponseFactory
import cl.donaton.donaton.factory.AuthResponse
import cl.donaton.donaton.security.JwtService

import cl.donaton.donaton.dto.LoginRequestDto
import cl.donaton.donaton.dto.UpdateUsernameRequestDto
import cl.donaton.donaton.dto.UpdateUsernameResponseDto

import cl.donaton.donaton.exception.BadCredentialsException
import cl.donaton.donaton.exception.NotFoundException
import cl.donaton.donaton.exception.UnauthorizedException

@Service
class AuthService(
    private val userRepository: UserRepository
    , private val jwtService: JwtService
) {
    
    @Transactional
    fun login(request: LoginRequestDto): AuthResponse {
        /* Obtiene el usuario que se quiere autenticar */
        val user = userRepository.findByUsername(request.username)

        /* Si no hay un usuario con ese username, lanza una excepción */
            ?: throw UnauthorizedException("Credenciales incorrectas")

            /* Si el password no coincide con el usuario buscado, lanza una excepción */
            if (user.password != request.password) {
                throw UnauthorizedException("Credenciales incorrectas")
            }

        /* Si el usuario existe y las credenciales son correctas, avanza
*
        Genera un token JWT */
        val token = jwtService.generateToken(user.id, user.username)
        
        /* Devuelve el usuario autenticado junto con el token generado */
        return AuthResponseFactory.createSuccessResponse(user, token)
    }

    @Transactional
    fun updateUsername(tokenHeader: String?, request: UpdateUsernameRequestDto): UpdateUsernameResponseDto {
        /* Validar que el header traiga el formato "Bearer <token> */
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            throw UnauthorizedException("No se proporcionó un token válido")
        }

        /* Extraer el token del header */
        val token = tokenHeader.substring(7)
        val userId = jwtService.extractUserId(token)
            ?: throw UnauthorizedException("Token inválido o expirado")
            
        /* Validar que el nuevo username no esté vacío */
        if (request.newUsername.isBlank()) {
            throw BadCredentialsException("El nuevo username no puede estar vacío")
        }

        /* Buscar al usuario por el ID que venía DENTRO del token */
        val user = userRepository.findById(userId).orElseThrow { 
            NotFoundException("Usuario no encontrado") 
        }

        /* Actualizar el username del usuario */
        user.username = request.newUsername
        userRepository.save(user)

        return UpdateUsernameResponseDto(
            userId = user.id,
            newUsername = user.username
        )
    }
}