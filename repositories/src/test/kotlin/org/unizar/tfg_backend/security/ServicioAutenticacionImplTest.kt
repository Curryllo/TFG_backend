package org.unizar.tfg_backend.security

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.core.userdetails.User
import org.unizar.tfg_backend.infraestrcuture.security.GeneradorTokenImpl
import org.unizar.tfg_backend.infraestrcuture.security.ServicioAutenticacionImpl
import org.unizar.tfg_backend.infraestrcuture.security.ServicioDetallesUsuario
import org.unizar.tfg_backend.infraestructure.repositories.RepositorioRefrescoToken
import org.unizar.tfg_backend.infraestructure.repositories.RepositorioUsuariosJpa

class ServicioAutenticacionImplTest {

    private val authManager: AuthenticationManager = Mockito.mock(AuthenticationManager::class.java)
    private val encoder: PasswordEncoder = Mockito.mock(PasswordEncoder::class.java)
    private val servicioDetallesUsuario: ServicioDetallesUsuario = mock()
    private val generadorToken: GeneradorTokenImpl = mock()
    private val repositorioRefrescoToken: RepositorioRefrescoToken = mock()
    private val repositorioUsuario: RepositorioUsuariosJpa = mock()
    private lateinit var servicio: ServicioAutenticacionImpl

    private val accessTokenExpiration: Long = 60000
    private val refreshTokenExpiration: Long = 120000

    private val email = "842545@unizar.es"
    private val password = "password"
    private val rol = "Admin"

    @BeforeEach
    fun setUp() {
        servicio = ServicioAutenticacionImpl(
            authManager,
            encoder,
            servicioDetallesUsuario,
            generadorToken,
            repositorioRefrescoToken,
            repositorioUsuario,
            accessTokenExpiration,
            refreshTokenExpiration,
        )

    }

    private fun buildUserDetails(email: String, rol: String) =
        User.builder()
            .username(email)
            .password("")
            .roles(rol)
            .build()



    @Test
    fun `autenticar un usuario existente`(){
        // --- ARRANGE (Preparación) ---
        val ud = buildUserDetails(email, rol)
        val tokenAccesoFalso = "access-token-mockeado"
        val tokenRefrescoFalso = "refresh-token-mockeado"

        val authenticationMock: Authentication = mock()

        // Le damos TODAS las reglas a los mocks antes de actuar
        whenever(authManager.authenticate(any())).thenReturn(authenticationMock)
        whenever(servicioDetallesUsuario.loadUserByUsername(email)).thenReturn(ud)
        whenever(generadorToken.generarToken(any(), any(), any())).thenReturn(tokenAccesoFalso)
        whenever(generadorToken.generarRefreshToken(any(), any())).thenReturn(tokenRefrescoFalso)

        // --- ACT (Acción) ---
        // Ahora sí, ejecutamos el servicio
        val tokens = servicio.autenticar(email, password)

        // --- ASSERT (Comprobación) ---
        assertTrue(tokens.tokenAcceso.isNotBlank())
        assertTrue(tokens.tokenRefresco.isNotBlank())

        assertEquals(tokenAccesoFalso, tokens.tokenAcceso)
        assertEquals(tokenRefrescoFalso, tokens.tokenRefresco)

        verify(repositorioRefrescoToken).save(eq(tokenRefrescoFalso), eq(ud))
    }

    @Test
    fun `refrescar devuelve dos tokens validos`(){
        val ud = buildUserDetails(email, rol)
        val oldRefresh = "refresh-token-viejo-mock"

        val nuevoAccesoFalso = "nuevo-access-token"
        val nuevoRefrescoFalso = "nuevo-refresh-token"

        whenever(generadorToken.extractEmail(oldRefresh)).thenReturn(email)
        whenever(repositorioRefrescoToken.findUserDetailsByToken(oldRefresh)).thenReturn(ud)
        whenever(servicioDetallesUsuario.loadUserByUsername(email)).thenReturn(ud)

        whenever(generadorToken.generarToken(any(), any(), any())).thenReturn(nuevoAccesoFalso)
        whenever(generadorToken.generarRefreshToken(any(), any())).thenReturn(nuevoRefrescoFalso)

        whenever(generadorToken.extractEmail(nuevoAccesoFalso)).thenReturn(email)
        whenever(generadorToken.extractEmail(nuevoRefrescoFalso)).thenReturn(email)



        val tokens = servicio.refrescarTokens(oldRefresh)

        assertNotEquals(oldRefresh, tokens.tokenRefresco)
        assertEquals(nuevoAccesoFalso, tokens.tokenAcceso)
        assertEquals(nuevoRefrescoFalso, tokens.tokenRefresco)


        verify(repositorioRefrescoToken).deleteByToken(oldRefresh)
        verify(repositorioRefrescoToken).save(eq(nuevoRefrescoFalso), eq(ud))

    }

    @Test
    fun `cerrarSesion elimina el refresh token de la BD`() {
        val refreshToken = "cualquier-token-aqui"

        servicio.cerrarSesion(refreshToken)

        verify(repositorioRefrescoToken).deleteByToken(refreshToken)
    }
}