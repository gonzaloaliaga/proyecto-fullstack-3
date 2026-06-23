package cl.donaton.donaton.security

import cl.donaton.donaton.security.JwtService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class JwtServiceTest {

    // Usamos comillas triples y limpiamos los espacios/saltos 
    // para evitar que Java tire IllegalArgumentException al decodificar
    private val privateKeyBase64 = """
        LS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tDQpNSUlFdlFJQkFEQU5CZ2txaGtpRzl3MEJBUUVGQUFTQ0JLY3dnZ1NqQWdFQUFvSUJBUURJMDhlbHN6Q0lhWEhHDQp0eU10REJ4ZUkrenFuS1RVc2hlb29yRW5jc2VYcDlJZWlFd3RDNFdCSmUxNU5ZbkV6SVlGUVo4bXV3NTZaVWlIDQowRGlmS2ZyN3YyczFVVkYxbTZyMExLak9iMkExT0huNnd4OHJONnliQW1lNXY2N0NjS1NDd0x3Nk1hSFJsR21EDQpDd3BrMWgrbXYwbGZkc2JlcEpKelhGclJ4b2xiUnIwbXBtTDY5SXQvQ3kzMDF3aFNYaDBOQ1NEeXZHLzIyZU52DQozRXh4eWdKaDdGdk9MV2hVZXozUWg5QXAxWkZOcGU3WGh2Z0EyUzhHdU5IKzlMVTNGRHBIMEt5TzNTYUpnQ0V1DQpTV01NYnhIV1lyR0lrd3ZsNW12YmNHQ3UydTVDalR2YmpodURDSGFITEtJcHAxYTNsRGlNVER1dmJjZVBkbjlODQpDcVFBZE1MQkFnTUJBQUVDZ2dFQUNuckpxZWNIUHZFUXJidlFCemxnOEJVc0YyU1A4ZVR1dU44dmd5VlpUcnZFDQpTbEZXdDBtSkFJK2wvQnViK2pQcGdQUDNEZjU0SDVFeU54c1puMDJob2hvdnFoODVrdHRxRVBhVkl2dWJINy9ZDQpGcXF3c1ZERkRpd3lsVFh3Vk1kcndFSjJPQXV2dStma2x5RGYvSm5UV2tCVVgvbks0Q0tpblVnNkdZVUVsU21NDQp4UXpTcko4UTVyNW1DMXZrdW1tNjYxR0JGUW9rZFVqVk9sQWhsRloyOStyNStDeHJiTmJJWktob3NmdjlCMVZ1DQpkUG5YTUJ4SW1IMEJYajNJak81SzdicDQvQ0pHWjdjelZmNmxyeVFqZUhLRXE1QmcxWCtIN2dEQ2FpU3A0SVI0DQpISjdmaC8vaEp4TWhCYkhjY1Q0WEdscFZmN3RHQUdURDZOYmpFaEEvU1FLQmdRRHNsQ2xnT1ZPWG5SSFJjOGx5DQpsbmMwOSsvdkM5ZEJkSk04ejNtMXFsWkJlTFhlZENuNmYwU1NXY2EwV3V0amM5anVEalI5Q0NDeC9xRzhTekkrDQpraHdFckF4dHdwVDhMdlUyM0M4RjAwUzMvYW9DQlRWQnVyU21jOXlmZkhacjJRelVuRmlwMUhJTUhuRmNrR3VoDQpDNjIvazRsYzFMNjhFc3NiRVQ3OFZNa1JhUUtCZ1FEWlVFZTIveEZsRkgwMzRVYkV6aCtSR3RjTVE3Z2dxQk5SDQpMUnQwaXo3WU1OaG5Sc1BOamhXVFBpMTNFUDF2V3EyODhmd0VwVkJiWFAwWG5IeVNoL2x5R2E3RHBFTFBib2ZoDQpPR3RLRmRGL1MrWCtPbDVHU2tVZUdRS3JqNjA5aWpkT09vODJ1U2lLL214c0JjUG1uckhzNjEzYWRmczQxbWx3DQpYd2ZmMnFnam1RS0JnR2Y1ZWZITFlCeTdNSENRWmRIUnBWU0hQMUVkTUI2WllIaVNhdGhYbzQ4a3dUR2lkaXljDQpzSU50cDNIdW41TlcvOWtvTHhOZ2RoWWtaZXErVmJmQU9VWW9ZMUg5K2NLWDFMZW1yNWZzMFQvWDRWYi8rUGNKDQpXb25wVStaWUtpZWQvaENYSWpTSnBSSUVjeWdPWk84amxkb2p1blF0SUNMQ1U3ZHlyZTd5U0dnNUFvR0FBak9tDQpCbHlmT0g3MHBkUktlMFA3WGVUL3VNZ1czWDNXZEVRWXV4Mk9EWEt4UHBxZzhUdzNTNzZkaDA5TXdnd1JKcER0DQpGTDc0Q1hUdnU0V0NYWm16RlV6ZFVHNGhueWhuaUFpaVRhWGE4WWM3VlZBU0ZIUThMd25oMVFzbm5qQUFUM0tZDQpjTjEvWTZrZ1FCZnUzQzlQYmk4VVRGcFZxVVhDMjdpYXc5SGJVa0VDZ1lFQTVHMHlBaHNIYW9lZnhIU3g2V0V6DQp1OElpZjdaRjlVWEFBUXBoVCs4dHdRMHVVV1RPUlljaXg4WXJwQTgrN3BGVndSUU15NlpvM2NXK2x0eGlPSTUvDQpWeCsybzRqL3Z0OWg5VmVORUUxaDJxZktlMDMzTC9BYzViaDM4YlM5cEVhYmV5QSt3K2tERnRKV0hIdXNBSmd2DQoyUmpUVDlaVC9ON3JCOWZDQ080TXU3Zz0NCi0tLS0tRU5EIFBSSVZBVEUgS0VZLS0tLS0NCg==
    """.replace("\\s".toRegex(), "")

    private val publicKeyBase64 = """
        LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0NCk1JSUJJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBUThBTUlJQkNnS0NBUUVBeU5QSHBiTXdpR2x4eHJjakxRd2MNClhpUHM2cHlrMUxJWHFLS3hKM0xIbDZmU0hvaE1MUXVGZ1NYdGVUV0p4TXlHQlVHZkpyc09lbVZJaDlBNG55bjYNCis3OXJOVkZSZFp1cTlDeW96bTlnTlRoNStzTWZLemVzbXdKbnViK3V3bkNrZ3NDOE9qR2gwWlJwZ3dzS1pOWWYNCnByOUpYM2JHM3FTU2MxeGEwY2FKVzBhOUpxWmkrdlNMZndzdDlOY0lVbDRkRFFrZzhyeHY5dG5qYjl4TWNjb0MNCllleGJ6aTFvVkhzOTBJZlFLZFdSVGFYdTE0YjRBTmt2QnJqUi92UzFOeFE2UjlDc2p0MG1pWUFoTGtsakRHOFINCjFtS3hpSk1MNWVacjIzQmdydHJ1UW8wNzI0NGJnd2gyaHl5aUthZFd0NVE0akV3N3IyM0hqM1ovVFFxa0FIVEMNCndRSURBUUFCDQotLS0tLUVORCBQVUJMSUMgS0VZLS0tLS0NCg==
    """.replace("\\s".toRegex(), "")
    
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