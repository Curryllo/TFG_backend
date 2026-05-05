@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure.delivery

import org.junit.jupiter.api.Assertions.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.doThrow
import org.springframework.http.HttpStatus
import org.unizar.tfg_backend.core.Usuario
import org.unizar.tfg_backend.core.usecases.AprobarSolicitudRegistroUseCase
import org.unizar.tfg_backend.core.usecases.EliminarUsuarioUseCase
import org.unizar.tfg_backend.core.usecases.ObtenerSolicitudesRegistroUseCase
import org.unizar.tfg_backend.core.usecases.ObtenerUsuariosActivosUseCase
import org.unizar.tfg_backend.core.usecases.RechazarSolicitudesRegistroUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class ControladorAdminTest {
    private val obtenerSolicitudesRegistroUseCase = mock<ObtenerSolicitudesRegistroUseCase>()
    private val rechazarSolicitudesRegistroUseCase = mock<RechazarSolicitudesRegistroUseCase>()
    private val aprobarSolicitudRegistroUseCase = mock<AprobarSolicitudRegistroUseCase>()
    private val eliminarUsuarioUseCase = mock<EliminarUsuarioUseCase>()
    private val obtenerUsuariosActivosUseCase = mock<ObtenerUsuariosActivosUseCase>()

    private val controlador = AdminController(
        obtenerSolicitudesRegistroUseCase,
        rechazarSolicitudesRegistroUseCase,
        aprobarSolicitudRegistroUseCase,
        eliminarUsuarioUseCase,
        obtenerUsuariosActivosUseCase
    )

    @Test
    fun `obtenerSolicitudesPendientes devuelve 200 con la lista de solicitudes`() {
        val usuario = Usuario(
            nombre = "Pepe",
            apellido1 = "Sanchez",
            apellido2 = "Perez",
            puesto = "Empleado",
            email = "pepe@unizar.es",
            rol = "Usuario",
            password = "password",
            estado = "Pendiente"
        )
        val lista = listOf(usuario)
        `when`(obtenerSolicitudesRegistroUseCase.obtenerSolicitudesRegistro()).thenReturn(lista)
        val respuesta = controlador.obtenerSolicitudesPendientes()
        assertEquals(respuesta.statusCode, HttpStatus.OK)
        assertEquals(lista, respuesta.body)
    }

    @Test
    fun `obtenerSolicitudesPendientes deuvelve 200 con la lista vacia`(){
        val lista = emptyList<Usuario>()
        `when`(obtenerSolicitudesRegistroUseCase.obtenerSolicitudesRegistro()).thenReturn(lista)
        val respuesta = controlador.obtenerSolicitudesPendientes()
        assertEquals(respuesta.statusCode, HttpStatus.OK)
        assertEquals(lista, respuesta.body)
    }

    @Test
    fun `obtenerUsuariosActivos devuelve 200 con la lista de usuarios`(){
        val usuario = Usuario(
            nombre = "Pepe",
            apellido1 = "Sanchez",
            apellido2 = "Perez",
            puesto = "Empleado",
            email = "pepe@unizar.es",
            rol = "Usuario",
            password = "password",
            estado = "Pendiente"
        )
        val lista = listOf(usuario)
        `when`(obtenerUsuariosActivosUseCase.obtenerUsuariosActivos()).thenReturn(lista)
        val respuesta = controlador.obtenerUsuariosActivos()
        assertEquals(respuesta.statusCode, HttpStatus.OK)
        assertEquals(lista, respuesta.body)
    }


    @Test
    fun `eliminarUsuario lanza NoSuchElementException cuando el email no existe`() {
        doThrow(NoSuchElementException("No se encontró ningún usuario con el email: noexiste@test.com"))
            .`when`(eliminarUsuarioUseCase).eliminarUsuario("noexiste@test.com")

        assertThrows(NoSuchElementException::class.java) {
            controlador.eliminarUsuario("noexiste@test.com")
        }
    }

    @Test
    fun `eliminarSolicitudPendiente lanza NoSuchElementException cuando el email no existe`() {
        doThrow(NoSuchElementException("No se encontró ninguna solicitud pendiente para el email: noexiste@test.com"))
            .`when`(rechazarSolicitudesRegistroUseCase).rechazarSolicitudesRegistro("noexiste@test.com")

        assertThrows(NoSuchElementException::class.java) {
            controlador.eliminarSolicitudPendiente("noexiste@test.com")
        }
    }

    @Test
    fun `aprobarSolicitudPendiente lanza NoSuchElementException cuando el email no existe`() {
        doThrow(NoSuchElementException("No se encontró ninguna solicitud pendiente para el email: noexiste@test.com"))
            .`when`(aprobarSolicitudRegistroUseCase).aprobarSolicitudRegistro("noexiste@test.com")

        assertThrows(NoSuchElementException::class.java) {
            controlador.aprobarSolicitudPendiente("noexiste@test.com")
        }
    }

    @Test
    fun `aprobarSolicitudPendiente devuelve 200 OK cuando el email existe`() {
        val respuesta = controlador.aprobarSolicitudPendiente("usuario@test.com")
        assertEquals(HttpStatus.OK, respuesta.statusCode)
    }

    @Test
    fun `eliminarSolicitudPendiente devuelve 200 OK cuando el email existe`() {
        val respuesta = controlador.eliminarSolicitudPendiente("usuario@test.com")
        assertEquals(HttpStatus.OK, respuesta.statusCode)
    }

    @Test
    fun `eliminarUsuario devuelve 200 OK cuando el email existe`() {
        val respuesta = controlador.eliminarUsuario("usuario@test.com")
        assertEquals(HttpStatus.OK, respuesta.statusCode)
    }


}