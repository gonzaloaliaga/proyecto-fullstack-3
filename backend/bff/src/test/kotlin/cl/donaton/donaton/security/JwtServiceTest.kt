import cl.donaton.donaton.security.JwtService
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.Base64
import kotlin.test.assertEquals
import kotlin.test.assertNull

class JwtServiceTest {

    private lateinit var jwtService: JwtService
    private lateinit var tokenValido: String

    @BeforeEach
    fun setup() {
        // 1. Inventamos un par de llaves matemáticas reales y puras en memoria
        val keyPairGen = KeyPairGenerator.getInstance("RSA")
        keyPairGen.initialize(2048)
        val keyPair = keyPairGen.generateKeyPair()

        val privateKey = keyPair.private as RSAPrivateKey
        val publicKey = keyPair.public as RSAPublicKey

        // 2. Disfrazamos la llave pública generada como si viniera del application.properties
        val purePublicBytesBase64 = Base64.getEncoder().encodeToString(publicKey.encoded)
        val pemString = "-----BEGIN PUBLIC KEY-----\n$purePublicBytesBase64\n-----END PUBLIC KEY-----"
        val publicKeyBase64Param = Base64.getEncoder().encodeToString(pemString.toByteArray())

        // 3. Le pasamos esta llave inmaculada al servicio BFF
        jwtService = JwtService(publicKeyBase64Param)

        // 4. Firmamos un token real usando la llave privada generada arriba
        val algorithm = Algorithm.RSA256(null, privateKey)
        tokenValido = JWT.create().withSubject("42").sign(algorithm)
    }

    @Test
    fun `validateAndExtractId con token valido debe retornar el userId`() {
        val result = jwtService.validateAndExtractId(tokenValido)
        assertEquals("42", result)
    }

    @Test
    fun `validateAndExtractId con token malformado debe retornar null`() {
        val result = jwtService.validateAndExtractId("esto.no.es.un.jwt")
        assertNull(result)
    }

    @Test
    fun `validateAndExtractId con token vacio debe retornar null`() {
        val result = jwtService.validateAndExtractId("")
        assertNull(result)
    }

    @Test
    fun `validateAndExtractId con token firmado con otra clave debe retornar null`() {
        val tokenDeOtraClave = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI5OSJ9.firma-invalida"
        val result = jwtService.validateAndExtractId(tokenDeOtraClave)
        assertNull(result)
    }
}