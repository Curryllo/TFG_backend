@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure.delivery

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.security.authentication.AuthenticationServiceException
import org.unizar.tfg_backend.core.TokensDominio
import org.unizar.tfg_backend.core.usecases.CerrarSesionUseCase
import org.unizar.tfg_backend.core.usecases.InicarSesionUseCase
import org.unizar.tfg_backend.core.usecases.RefrescarTokenUseCase
import org.unizar.tfg_backend.core.usecases.RegistrarUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.core.userdetails.UsernameNotFoundException

class ControladorAuthTest {

    private val iniciarSesionUseCase = mock<InicarSesionUseCase>()
    private val registrarUseCase = mock<RegistrarUseCase>()
    private val refrescarTokenUseCase = mock<RefrescarTokenUseCase>()
    private val cerrarSesionUseCase = mock<CerrarSesionUseCase>()

    private val controlador = AuthController(
        iniciarSesionUseCase,
        registrarUseCase,
        refrescarTokenUseCase,
        cerrarSesionUseCase
    )

    @Test
    fun `login autentica con éxito`(){
        val request  = LoginIn(
            "842545@unizar.es",
            "password"
        )
        val tokens = TokensDominio(
            "access-token",
            "refresh-token"
        )
        `when`(iniciarSesionUseCase.iniciarSesion(request.mail, request.password)).thenReturn(tokens)
        val response = controlador.login(request)
        assertEquals(response.tokenAcceso, tokens.tokenAcceso)
        assertEquals(response.tokenRefresco, tokens.tokenRefresco)
    }

    @Test
    fun `login lanza BadCredentialsException cuando la contrasena es incorrecta`() {
        `when`(iniciarSesionUseCase.iniciarSesion("usuario@test.com", "wrongpassword"))
            .thenThrow(BadCredentialsException("Credenciales incorrectas"))

        assertThrows(BadCredentialsException::class.java) {
            controlador.login(LoginIn(mail = "usuario@test.com", password = "wrongpassword"))
        }
    }

    @Test
    fun `login lanza UsernameNotFoundException cuando el usuario no existe`() {
        `when`(iniciarSesionUseCase.iniciarSesion("noexiste@test.com", "password123"))
            .thenThrow(UsernameNotFoundException("No se encontró el usuario"))

        assertThrows(UsernameNotFoundException::class.java) {
            controlador.login(LoginIn(mail = "noexiste@test.com", password = "password123"))
        }
    }

    @Test
    fun `login lanza DisabledException cuando la cuenta no esta activa`() {
        `when`(iniciarSesionUseCase.iniciarSesion("inactivo@test.com", "password123"))
            .thenThrow(DisabledException("Cuenta deshabilitada"))

        assertThrows(DisabledException::class.java) {
            controlador.login(LoginIn(mail = "inactivo@test.com", password = "password123"))
        }
    }

    @Test
    fun `refresh recarga con éxito los tokens`(){
        val tokens = TokensDominio(
            "nuevo-token-access",
            "nuevo-token-refresh"
        )
        `when`(refrescarTokenUseCase.refrescarTokens("refresh-token")).thenReturn(tokens)
        val response = controlador.refresh("refresh-token")
        assertEquals(response.tokenAcceso, tokens.tokenAcceso)
        assertEquals(response.tokenRefresco, tokens.tokenRefresco)
    }

    @Test
    fun `refresh no recarga los tokens con exito debido a token de refresco invalido`(){
        `when`(refrescarTokenUseCase.refrescarTokens("refresh-token-invalido"))
            .thenThrow(AuthenticationServiceException("Invalid refresh token"))

        assertThrows(AuthenticationServiceException::class.java) {
            controlador.refresh("refresh-token-invalido")
        }
    }

    @Test
    fun `logout llama al use case con el refresh token correcto`() {
        controlador.logout("refresh-token-456")

        verify(cerrarSesionUseCase, times(1)).cerrarSesion("refresh-token-456")
    }

    @Test
    fun `logout no lanza excepcion con un token valido`() {
        assertDoesNotThrow {
            controlador.logout("refresh-token-456")
        }
    }

    @Test
    fun `singIn registra con éxito`(){
        val datos = SingIn(
            nombre = "Pepe",
            apellido1 = "Sanchez",
            apellido2 = "Sanchez",
            puesto = "Empleado",
            email = "pepe@unizar.es",
            rol = "usuario",
            password = "password123",
        )
        controlador.singIn(datos)
        val dominio = datos.toDomain()
        verify(registrarUseCase, times(1)).registrar(dominio)
    }

    @Test
    fun `singIn no lanza excepcion con datos validos`() {
        val request = SingIn(
            nombre = "Juan",
            apellido1 = "García",
            apellido2 = "López",
            puesto = "Médico",
            email = "juan@test.com",
            rol = "USER",
            password = "password123"
        )

        assertDoesNotThrow {
            controlador.singIn(request)
        }
    }

}