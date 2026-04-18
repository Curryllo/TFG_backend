@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.security

import io.jsonwebtoken.ExpiredJwtException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.unizar.tfg_backend.infraestrcuture.security.GeneradorTokenImpl

class GeneradorTokenImplTest {

    private val generador = GeneradorTokenImpl()

    private val email = "842545@unizar.es"
    private val rol = "Admin"

    @Test
    fun `generar token devuelve un token no vacio`() {
        val token = generador.generarToken(email, rol, 60_000)
        assertTrue(token.isNotBlank())
        assertEquals(2, token.count { it == '.' })
    }

    @Test
    fun `extraer email devuelve el email correcto`() {
        val token = generador.generarToken(email, rol, 60_000)
        val emailExtraido = generador.extractEmail(token)
        assertEquals(emailExtraido, email)
    }

    @Test
    fun `token con expiracion 0 ms caduca inmediatamente`() {
        val token = generador.generarToken(email, rol, 0)
        // Pequeña espera para garantizar que el reloj avanza
        Thread.sleep(10)
        assertThrows<ExpiredJwtException> {
            generador.extractEmail(token)
        }
    }

    @Test
    fun `tokens generados para el mismo usuario son distintos (iat diferente)`() {
        val token1 = generador.generarToken(email, rol, 60_000)
        Thread.sleep(1100) // iat granularidad segundos en JJWT
        val token2 = generador.generarToken(email, rol, 60_000)
        assertNotEquals(token1, token2)
    }

    @Test
    fun `refrescar token devuelve un token no vacio`() {
        val token = generador.generarRefreshToken(email, 600_000)
        assertTrue(token.isNotBlank())
        assertEquals(2, token.count { it == '.' })
    }

    @Test
    fun `refresh token caducado lanza ExpiredJwtException`() {
        val token = generador.generarRefreshToken(email, 0)
        Thread.sleep(10)
        assertThrows<ExpiredJwtException> {
            generador.extractEmail(token)
        }
    }


    @Test
    fun `token de acceso y refresh token son independientes`() {
        val access = generador.generarToken("a@b.com", "USER", 60_000)
        val refresh = generador.generarRefreshToken("a@b.com", 60_000)
        assertNotEquals(access, refresh)
        // Ambos deben resolver al mismo email
        assertEquals(generador.extractEmail(access), generador.extractEmail(refresh))
    }
}