package cl.donaton.donaton.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

@Service
class JwtService(
    @Value("\${JWT_PUBLIC_KEY}") private val publicKeyBase64: String
) {
    private val algorithm: Algorithm by lazy {
        Algorithm.RSA256(getPublicKey(), null)
    }

    private val verifier by lazy {
        JWT.require(algorithm).build()
    }

    private fun unpackKey(envBase64: String): String {
        val pemString = String(Base64.getDecoder().decode(envBase64))
        return pemString
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\\s".toRegex(), "")
    }

    private fun getPublicKey(): RSAPublicKey {
        val pureBase64 = unpackKey(publicKeyBase64)
        val keyBytes = Base64.getDecoder().decode(pureBase64)
        val spec = X509EncodedKeySpec(keyBytes)
        val kf = KeyFactory.getInstance("RSA")
        return kf.generatePublic(spec) as RSAPublicKey
    }

    fun validateAndExtractId(token: String): String? {
        return try {
            val decodedJWT = verifier.verify(token)
            decodedJWT.subject
        } catch (e: Exception) {
            println("Error validando token: ${e.message}")
            null
        }
    }
}