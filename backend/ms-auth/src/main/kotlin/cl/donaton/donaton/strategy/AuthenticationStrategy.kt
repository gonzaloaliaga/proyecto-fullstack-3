package cl.donaton.donaton.strategy

import cl.donaton.donaton.model.User

interface AuthenticationStrategy {
    fun authenticate(inputPass: String, user: User): Boolean
}

/**
 * Estrategia de Desarrollo: Comparación directa pero con validación de nulidad.
 */
class SimplePasswordStrategy : AuthenticationStrategy {
    override fun authenticate(inputPass: String, user: User): Boolean {
        return inputPass.isNotBlank() && inputPass == user.password
    }
}

/**
 * Estrategia de "Producción Temprana": 
 * Valida longitud y que el username no sea igual a la password.
 */
class EnhancedSecurityStrategy : AuthenticationStrategy {
    override fun authenticate(inputPass: String, user: User): Boolean {
        val isCorrect = inputPass == user.password
        val isLongEnough = inputPass.length >= 4
        val isNotUsername = inputPass != user.username
        
        return isCorrect && isLongEnough && isNotUsername
    }
}