package cl.donaton.donaton.service

import cl.donaton.donaton.client.AuthClient
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity
import kotlin.test.assertEquals

class BffAuthServiceTest {

    /* se simula el cliente real */
    private val authClient = mockk<AuthClient>()
    
    /* se inyecta el mock en el servicio real que se esta probando */
    private val bffAuthService = BffAuthService(authClient)

    @Test
    fun `login deberia derivar la peticion al authClient y retornar su respuesta`() {
        /* arrange */
        val bodyMock = """{"email":"test@correo.com", "password":"123"}"""
        val respuestaEsperada = ResponseEntity.ok("TokenGenerado123")
        
        /* se le enseña al mock como debe comportarse cuando lo llamen */
        every { authClient.forwardLogin(bodyMock) } returns respuestaEsperada

        val resultado = bffAuthService.login(bodyMock)

        /* se comprueba que el resultado sea el que entrego el mock */
        assertEquals(respuestaEsperada, resultado) 
        
        /* se verifica que el servicio realmente haya usado el authClient con ese parametro exacto */
        verify(exactly = 1) { authClient.forwardLogin(bodyMock) } 
    }

    @Test
    fun `updateUsername deberia derivar la peticion al authClient y retornar su respuesta`() {
        /* Arrange */
        val headerMock = "Bearer algun-token-jwt"
        val bodyMock = """{"username":"nuevoUsuario"}"""
        val respuestaEsperada = ResponseEntity.ok("Usuario actualizado con éxito")
        
        /* se le enseña al mock que responder */
        every { authClient.forwardUpdateUsername(headerMock, bodyMock) } returns respuestaEsperada

        val resultado = bffAuthService.updateUsername(headerMock, bodyMock)

        assertEquals(respuestaEsperada, resultado)
        verify(exactly = 1) { authClient.forwardUpdateUsername(headerMock, bodyMock) }
    }
}