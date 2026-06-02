package cl.donaton.donaton.controller

import cl.donaton.donaton.security.JwtService
import cl.donaton.donaton.factory.AuthResponseFactory
import cl.donaton.donaton.repository.UserRepository
import cl.donaton.donaton.strategy.AuthenticationStrategy
import cl.donaton.donaton.strategy.SimplePasswordStrategy
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userRepository: UserRepository,
    private val jwtService: JwtService // Inyectar el servicio
) {
    private val authStrategy: AuthenticationStrategy = SimplePasswordStrategy()

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: Map<String, String>): ResponseEntity<Any> {
        val username = loginRequest["username"] ?: ""
        val password = loginRequest["password"] ?: ""

        val user = userRepository.findByUsername(username)

        return if (user != null && authStrategy.authenticate(password, user)) {
            // Generar el token y devolverlo
            val token = jwtService.generateToken(user.id, user.username)
            val response = AuthResponseFactory.createSuccessResponse(user, token)
            ResponseEntity.ok(response)
            
        } else {
            ResponseEntity.status(401).body(mapOf("message" to "Credenciales incorrectas"))
        }
    }

    @PostMapping("/update-username")
    fun updateUsername(
        @RequestHeader("Authorization") authHeader: String?,
        @RequestBody request: Map<String, String>
    ): ResponseEntity<Any> {
        
        // 1. Validar que el header traiga el formato "Bearer <token>"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(mapOf("message" to "No se proporcionó un token válido"))
        }

        val token = authHeader.substring(7)
        val userId = jwtService.extractUserId(token)

        // 2. Si el token es inválido, rechazar inmediatamente
        if (userId == null) {
            return ResponseEntity.status(401).body(mapOf("message" to "Token inválido o expirado"))
        }

        // 3. Buscar al usuario por el ID que venía DENTRO del token
        val user = userRepository.findById(userId).orElse(null)
        val newUsername = request["newUsername"] ?: ""

        return if (user != null && newUsername.isNotBlank()) {
            user.username = newUsername
            userRepository.save(user)
            ResponseEntity.ok(mapOf(
                "message" to "Nombre de usuario actualizado",
                "username" to newUsername
            ))
        } else {
            ResponseEntity.status(404).body(mapOf("message" to "Usuario no encontrado o datos inválidos"))
        }
    }
}