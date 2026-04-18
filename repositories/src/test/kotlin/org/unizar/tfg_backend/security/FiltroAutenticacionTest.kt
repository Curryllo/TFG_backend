@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.security

import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.unizar.tfg_backend.infraestrcuture.security.FiltroAutenticacion
import org.unizar.tfg_backend.infraestrcuture.security.GeneradorTokenImpl
import org.unizar.tfg_backend.infraestrcuture.security.ServicioDetallesUsuario

class FiltroAutenticacionTest {

    private val email = "842545@unizar.es"

    private val TOKEN_VALIDO = "token-valido-mock"
    private val TOKEN_CADUCADO = "token-caducado-mock"

    private val generadorToken: GeneradorTokenImpl = mock()
    private val servicioDetallesUsuario: ServicioDetallesUsuario = mock()

    private val filtroAutenticacion = FiltroAutenticacion(servicioDetallesUsuario, generadorToken)

    private fun buildUserDetails(email: String) =
        User.builder().username(email).password("1234").roles("Admin").build()


    private fun requestConToken(token: String): MockHttpServletRequest {
        val req = MockHttpServletRequest()
        req.addHeader("Authorization", "Bearer $token")
        return req
    }

    @BeforeEach
    fun setUp() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `sin cabecera auth no autentica`() {
        val req = MockHttpServletRequest()
        val res = MockHttpServletResponse()
        val chain: FilterChain = mock()

        filtroAutenticacion.doFilterInternal(req, res, chain)

        verify(chain).doFilter(req, res)
        assertNull(SecurityContextHolder.getContext().authentication)
        assertEquals(200, res.status)
    }

    @Test
    fun `cabecera sin prefijo bearer no autentica`() {
        val req = MockHttpServletRequest()
        req.addHeader("Authorization", "Basic dXNlcjpwYXNz")
        val res = MockHttpServletResponse()
        val chain: FilterChain = mock()

        filtroAutenticacion.doFilterInternal(req, res, chain)

        verify(chain).doFilter(req, res)
        assertNull(SecurityContextHolder.getContext().authentication)
    }


    @Test
    fun `token valido autentica al usuario en el SecurityContext`() {
        val ud = buildUserDetails(email)
        whenever(generadorToken.extractEmail(TOKEN_VALIDO)).thenReturn(email)
        whenever(servicioDetallesUsuario.loadUserByUsername(email)).thenReturn(ud)


        val req = requestConToken(TOKEN_VALIDO)
        val res = MockHttpServletResponse()
        val chain: FilterChain = mock()

        filtroAutenticacion.doFilterInternal(req, res, chain)

        verify(chain).doFilter(req, res)
        assertNotNull(SecurityContextHolder.getContext().authentication)
        assertEquals(email, SecurityContextHolder.getContext().authentication.name)
        assertEquals(200, res.status)
    }

    @Test
    fun `token valido llama a loadUserByUsername exactamente una vez`() {
        val ud = buildUserDetails(email)
        whenever(generadorToken.extractEmail(TOKEN_VALIDO)).thenReturn(email)
        whenever(servicioDetallesUsuario.loadUserByUsername(email)).thenReturn(ud)

        filtroAutenticacion.doFilterInternal(requestConToken(TOKEN_VALIDO), MockHttpServletResponse(), mock())

        verify(servicioDetallesUsuario, times(1)).loadUserByUsername(email)
    }

    @Test
    fun `token caducado devuelve 401 y no continua la cadena`() {
        whenever(generadorToken.extractEmail(TOKEN_CADUCADO)).thenThrow(
            ExpiredJwtException(null, null, "Token caducado simulado")
        )

        val req = requestConToken(TOKEN_CADUCADO)
        val res = MockHttpServletResponse()
        val chain: FilterChain = mock()

        filtroAutenticacion.doFilterInternal(req, res, chain)

        assertEquals(401, res.status)
        verify(chain, never()).doFilter(any(), any()) // Aseguramos que la petición se detiene
        assertNull(SecurityContextHolder.getContext().authentication)
    }

    @Test
    fun `token caducado devuelve body JSON con mensaje de error`() {
        whenever(generadorToken.extractEmail(TOKEN_CADUCADO)).thenThrow(
            ExpiredJwtException(null, null, "Token caducado simulado")
        )

        val res = MockHttpServletResponse()

        filtroAutenticacion.doFilterInternal(requestConToken(TOKEN_CADUCADO), res, mock())

        val body = res.contentAsString
        assertTrue(body.contains("error"), "El body debe contener la palabra 'error'")
    }

    @Test
    fun `si el contexto ya tiene autenticacion no vuelve a cargar el usuario`() {
        val ud = buildUserDetails(email)
        whenever(generadorToken.extractEmail(TOKEN_VALIDO)).thenReturn(email)
        whenever(servicioDetallesUsuario.loadUserByUsername(email)).thenReturn(ud)

        // Primera pasada — autentica y guarda en el SecurityContext
        filtroAutenticacion.doFilterInternal(requestConToken(TOKEN_VALIDO), MockHttpServletResponse(), mock())

        // Segunda pasada con el mismo token
        filtroAutenticacion.doFilterInternal(requestConToken(TOKEN_VALIDO), MockHttpServletResponse(), mock())

        // El usuario solo se carga la primera vez
        verify(servicioDetallesUsuario, times(1)).loadUserByUsername(email)
    }
}