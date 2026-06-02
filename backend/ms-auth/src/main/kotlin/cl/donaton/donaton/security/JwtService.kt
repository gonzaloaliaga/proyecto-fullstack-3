package cl.donaton.donaton.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import java.util.Date

@Service
class JwtService(
    @Value("\${JWT_PRIVATE_KEY}") private val privateKeyBase64: String,
    @Value("\${JWT_PUBLIC_KEY}") private val publicKeyBase64: String
) {
    private val algorithm: Algorithm by lazy {
        Algorithm.RSA256(getPublicKey(), getPrivateKey())
    }

    private val verifier by lazy {
        JWT.require(algorithm).build()
    }

    private fun unpackKey(envBase64: String): String {
        val pemString = String(Base64.getDecoder().decode(envBase64))
        return pemString
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("-----BEGIN RSA PRIVATE KEY-----", "")
            .replace("-----END RSA PRIVATE KEY-----", "")
            .replace("\\s".toRegex(), "") // Elimina saltos de línea y espacios
    }

    private fun getPrivateKey(): RSAPrivateKey {
        val pureBase64 = unpackKey(privateKeyBase64)
        val keyBytes = Base64.getDecoder().decode(pureBase64)
        val spec = PKCS8EncodedKeySpec(keyBytes)
        val kf = KeyFactory.getInstance("RSA")
        return kf.generatePrivate(spec) as RSAPrivateKey
    }

    private fun getPublicKey(): RSAPublicKey {
        val pureBase64 = unpackKey(publicKeyBase64)
        val keyBytes = Base64.getDecoder().decode(pureBase64)
        val spec = X509EncodedKeySpec(keyBytes)
        val kf = KeyFactory.getInstance("RSA")
        return kf.generatePublic(spec) as RSAPublicKey
    }

    fun generateToken(id: Long, username: String): String {
        return JWT.create()
            .withSubject(id.toString())
            .withClaim("username", username)
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + 86400000))
            .sign(algorithm)
    }

    fun extractUserId(token: String): Long? {
        return try {
            val decodedJWT = verifier.verify(token)
            decodedJWT.subject.toLong()
        } catch (e: Exception) {
            null
        }
    }
}