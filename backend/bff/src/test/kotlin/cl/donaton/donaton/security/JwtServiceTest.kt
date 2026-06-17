package cl.donaton.donaton.security

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class JwtServiceTest {

    private val publicKeyBase64 = "BASE64_DE_TU_PUBLIC_KEY_DE_PRUEBA"

    private val tokenValido = "TOKEN_GENERADO_CON_TU_CLAVE_PRIVADA_DE_PRUEBA"

    private lateinit var jwtService: JwtService

    @BeforeEach
    fun setup() {
        jwtService = JwtService(publicKeyBase64)
    }

    @Test
    fun `validateAndExtractId con token valido debe retornar el userId`() {
        val result = jwtService.validateAndExtractId(tokenValido)

        assertNotNull(result)
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