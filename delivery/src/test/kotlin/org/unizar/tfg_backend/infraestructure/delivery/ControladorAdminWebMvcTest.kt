@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure.delivery

import com.fasterxml.jackson.databind.ObjectMapper
import org.mockito.Mockito.`when`
import org.mockito.kotlin.doThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.unizar.tfg_backend.core.usecases.AprobarSolicitudRegistroUseCase
import org.unizar.tfg_backend.core.usecases.EliminarUsuarioUseCase
import org.unizar.tfg_backend.core.usecases.ObtenerSolicitudesRegistroUseCase
import org.unizar.tfg_backend.core.usecases.ObtenerUsuariosActivosUseCase
import org.unizar.tfg_backend.core.usecases.RechazarSolicitudesRegistroUseCase
import kotlin.test.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.unizar.tfg_backend.core.Usuario

@WebMvcTest(controllers = [AdminController::class])
@AutoConfigureMockMvc(addFilters = false)
class ControladorAdminWebMvcTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var obtenerSolicitudesRegistroUseCase: ObtenerSolicitudesRegistroUseCase
    @MockitoBean lateinit var rechazarSolicitudesRegistroUseCase: RechazarSolicitudesRegistroUseCase
    @MockitoBean lateinit var aprobarSolicitudRegistroUseCase: AprobarSolicitudRegistroUseCase
    @MockitoBean lateinit var eliminarUsuarioUseCase: EliminarUsuarioUseCase
    @MockitoBean lateinit var obtenerUsuariosActivosUseCase: ObtenerUsuariosActivosUseCase

    private val mapper = ObjectMapper()

    // --- GET /api/admin/solicitudes ---

    @Test
    fun `GET solicitudes devuelve 200 con lista de solicitudes`() {
        val lista = listOf(usuarioBase())
        `when`(obtenerSolicitudesRegistroUseCase.obtenerSolicitudesRegistro()).thenReturn(lista)

        mockMvc.perform(get("/api/admin/solicitudes"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].email").value("juan@test.com"))
    }

    @Test
    fun `GET solicitudes devuelve 200 con lista vacia`() {
        `when`(obtenerSolicitudesRegistroUseCase.obtenerSolicitudesRegistro()).thenReturn(emptyList())

        mockMvc.perform(get("/api/admin/solicitudes"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isEmpty)
    }

    // --- POST /api/admin/solicitudes/{email}/aprobar ---

    @Test
    fun `POST aprobar devuelve 200 cuando el email existe`() {
        mockMvc.perform(post("/api/admin/solicitudes/juan@test.com/aprobar"))
            .andExpect(status().isOk)
    }

    @Test
    fun `POST aprobar devuelve 500 cuando el email no existe`() {
        doThrow(NoSuchElementException("No encontrado"))
            .`when`(aprobarSolicitudRegistroUseCase).aprobarSolicitudRegistro("noexiste@test.com")

        org.junit.jupiter.api.Assertions.assertThrows(jakarta.servlet.ServletException::class.java) {
            mockMvc.perform(post("/api/admin/solicitudes/noexiste@test.com/aprobar"))
        }
    }

    // --- DELETE /api/admin/solicitudes/{email}/rechazar ---

    @Test
    fun `DELETE rechazar devuelve 200 cuando el email existe`() {
        mockMvc.perform(delete("/api/admin/solicitudes/juan@test.com/rechazar"))
            .andExpect(status().isOk)
    }

    @Test
    fun `DELETE rechazar devuelve 500 cuando el email no existe`() {
        doThrow(NoSuchElementException("No encontrado"))
            .`when`(rechazarSolicitudesRegistroUseCase).rechazarSolicitudesRegistro("noexiste@test.com")

        org.junit.jupiter.api.Assertions.assertThrows(jakarta.servlet.ServletException::class.java) {
            mockMvc.perform(delete("/api/admin/solicitudes/noexiste@test.com/rechazar"))
        }
    }

    // --- DELETE /api/admin/eliminar/{email} ---

    @Test
    fun `DELETE eliminarUsuario devuelve 200 cuando el email existe`() {
        mockMvc.perform(delete("/api/admin/eliminar/juan@test.com"))
            .andExpect(status().isOk)
    }

    @Test
    fun `DELETE eliminarUsuario devuelve 500 cuando el email no existe`() {
        doThrow(NoSuchElementException("No encontrado"))
            .`when`(eliminarUsuarioUseCase).eliminarUsuario("noexiste@test.com")

        org.junit.jupiter.api.Assertions.assertThrows(jakarta.servlet.ServletException::class.java) {
            mockMvc.perform(delete("/api/admin/eliminar/noexiste@test.com"))
        }
    }

    // --- GET /api/admin/usuarios ---

    @Test
    fun `GET usuarios devuelve 200 con lista de usuarios activos`() {
        val lista = listOf(usuarioBase())
        `when`(obtenerUsuariosActivosUseCase.obtenerUsuariosActivos()).thenReturn(lista)

        mockMvc.perform(get("/api/admin/usuarios"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].email").value("juan@test.com"))
    }

    @Test
    fun `GET usuarios devuelve 200 con lista vacia`() {
        `when`(obtenerUsuariosActivosUseCase.obtenerUsuariosActivos()).thenReturn(emptyList())

        mockMvc.perform(get("/api/admin/usuarios"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isEmpty)
    }

    // --- Helper ---

    private fun usuarioBase() = Usuario(
        nombre = "Juan",
        apellido1 = "García",
        apellido2 = "López",
        puesto = "Médico",
        email = "juan@test.com",
        rol = "USER",
        password = "password123",
        estado = "Pendiente"
    )
}