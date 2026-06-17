package cl.donaton.donaton.security

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class JwtServiceTest {

    private val privateKeyBase64 = "BASE64_DE_TU_PRIVATE_KEY_DE_PRUEBA"

    private val publicKeyBase64 = "BASE64_DE_TU_PUBLIC_KEY_DE_PRUEBA"

    private lateinit var jwtService: JwtService

    @BeforeEach
    fun setup() {
        jwtService = JwtService(privateKeyBase64, publicKeyBase64)
    }

    @Test
    fun `generateToken debe retornar un token no nulo`() {
        val token = jwtService.generateToken(1L, "admin")

        assertNotNull(token)
    }

    @Test
    fun `generateToken y extractUserId deben ser consistentes`() {
        val token = jwtService.generateToken(42L, "usuario-prueba")

        val userId = jwtService.extractUserId(token)

        assertEquals(42L, userId)
    }

    @Test
    fun `extractUserId con token malformado debe retornar null`() {
        val result = jwtService.extractUserId("token.invalido.aqui")

        assertNull(result)
    }

    @Test
    fun `extractUserId con string vacio debe retornar null`() {
        val result = jwtService.extractUserId("")

        assertNull(result)
    }

    @Test
    fun `generateToken con distintos usuarios debe generar tokens distintos`() {
        val token1 = jwtService.generateToken(1L, "admin")
        val token2 = jwtService.generateToken(2L, "logistica")

        assertNotNull(token1)
        assertNotNull(token2)
        assert(token1 != token2)
    }

    @Test
    fun `extractUserId con token firmado con clave distinta debe retornar null`() {
        val tokenFalso = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxIn0.firma-incorrecta"

        val result = jwtService.extractUserId(tokenFalso)

        assertNull(result)
    }
}