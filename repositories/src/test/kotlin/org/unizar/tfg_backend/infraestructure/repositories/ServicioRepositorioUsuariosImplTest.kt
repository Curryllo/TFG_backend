@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure.repositories

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
open class ServicioRepositorioUsuariosImplTest {

    @Autowired
    lateinit var repositorioJpa: RepositorioUsuariosJpa

    private lateinit var servicio: ServicioRepositorioUsuariosImpl

    @BeforeEach
    fun setup() {
        repositorioJpa.deleteAll()
        servicio = ServicioRepositorioUsuariosImpl(repositorioJpa)
    }

    @Test
    fun `listadoSolicitudesRegistro devuelve solo usuarios con estado Pendiente`() {
        repositorioJpa.save(entidadUsuario("pendiente@test.com", "Pendiente"))
        repositorioJpa.save(entidadUsuario("activo@test.com", "Activo"))

        val resultado = servicio.listadoSolicitudesRegistro()

        assertEquals(1, resultado.size)
        assertEquals("pendiente@test.com", resultado[0].email)
    }

    @Test
    fun `listadoSolicitudesRegistro devuelve lista vacia cuando no hay solicitudes pendientes`() {
        repositorioJpa.save(entidadUsuario("activo@test.com", "Activo"))

        val resultado = servicio.listadoSolicitudesRegistro()

        assertTrue(resultado.isEmpty())
    }

    @Test
    fun `aprobarSolicitudRegistro cambia el estado a Activo`() {
        repositorioJpa.save(entidadUsuario("pendiente@test.com", "Pendiente"))

        servicio.aprobarSolictudRegistro("pendiente@test.com")

        val usuario = repositorioJpa.findByEmail("pendiente@test.com")
        assertEquals("Activo", usuario?.estado)
    }

    @Test
    fun `aprobarSolicitudRegistro lanza NoSuchElementException cuando el email no existe`() {
        assertThrows(NoSuchElementException::class.java) {
            servicio.aprobarSolictudRegistro("noexiste@test.com")
        }
    }

    @Test
    fun `rechazarSolicitudRegistro elimina el usuario de la base de datos`() {
        repositorioJpa.save(entidadUsuario("pendiente@test.com", "Pendiente"))

        servicio.rechazarSolicitudRegistro("pendiente@test.com")

        assertNull(repositorioJpa.findByEmail("pendiente@test.com"))
    }

    @Test
    fun `rechazarSolicitudRegistro lanza NoSuchElementException cuando el email no existe`() {
        assertThrows(NoSuchElementException::class.java) {
            servicio.rechazarSolicitudRegistro("noexiste@test.com")
        }
    }

    @Test
    fun `eliminarUsuario elimina el usuario de la base de datos`() {
        repositorioJpa.save(entidadUsuario("activo@test.com", "Activo"))

        servicio.eliminarUsuario("activo@test.com")

        assertNull(repositorioJpa.findByEmail("activo@test.com"))
    }

    @Test
    fun `eliminarUsuario lanza NoSuchElementException cuando el email no existe`() {
        assertThrows(NoSuchElementException::class.java) {
            servicio.eliminarUsuario("noexiste@test.com")
        }
    }

    @Test
    fun `listadoUsuariosActivos devuelve solo usuarios con estado Activo`() {
        repositorioJpa.save(entidadUsuario("activo@test.com", "Activo"))
        repositorioJpa.save(entidadUsuario("pendiente@test.com", "Pendiente"))

        val resultado = servicio.listadoUsuariosActivos()

        assertEquals(1, resultado.size)
        assertEquals("activo@test.com", resultado[0].email)
    }

    @Test
    fun `listadoUsuariosActivos devuelve lista vacia cuando no hay usuarios activos`() {
        repositorioJpa.save(entidadUsuario("pendiente@test.com", "Pendiente"))

        val resultado = servicio.listadoUsuariosActivos()

        assertTrue(resultado.isEmpty())
    }

    // --- Helper ---

    private fun entidadUsuario(email: String, estado: String) = EntidadUsuario(
        idUsuario = null,
        nombre = "Juan",
        apellido1 = "García",
        apellido2 = "López",
        puesto = "Médico",
        email = email,
        rol = "USER",
        password = "password123",
        estado = estado
    )
}