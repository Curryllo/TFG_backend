package org.unizar.tfg_backend.infraestructure.repositories

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
open class RepositorioFormularioMonitoreoJpaTest {

    @Autowired
    lateinit var repositorio: RepositorioFormularioMonitoreoJpa

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setup() {
        repositorio.deleteAll()
    }

    @Test
    fun `save guarda un formulario de monitoreo y le asigna un id`() {
        val entidad = entidadBase()
        val guardada = repositorio.save(entidad)
        assertNotNull(guardada.idForm)
    }

    @Test
    fun `findAll devuelve todos los formularios guardados`() {
        repositorio.save(entidadBase())
        repositorio.save(entidadBase())
        val resultado = repositorio.findAll()
        assertEquals(2, resultado.size)
    }

    @Test
    fun `findAll devuelve lista vacia cuando no hay formularios`() {
        val resultado = repositorio.findAll()
        assertTrue(resultado.isEmpty())
    }

    @Test
    fun `buscarVectoresEnRadio devuelve vectores dentro del radio`() {
        // Zaragoza centro
        repositorio.save(entidadConCoordenadas(41.65, -0.87))
        // Madrid, fuera del radio de 10km
        repositorio.save(entidadConCoordenadas(40.41, -3.70))

        val resultado = repositorio.findVectoresEnRadio(41.65, -0.87, 10.0)

        assertEquals(1, resultado.size)
    }

    @Test
    fun `buscarVectoresEnRadio no devuelve vectores sin coordenadas`() {
        repositorio.save(entidadBase()) // sin coordenadas
        repositorio.save(entidadConCoordenadas(41.65, -0.87))

        val resultado = repositorio.findVectoresEnRadio(41.65, -0.87, 10.0)

        assertEquals(1, resultado.size)
    }

    @Test
    fun `buscarVectoresEnRadio devuelve lista vacia cuando no hay vectores cercanos`() {
        repositorio.save(entidadConCoordenadas(40.41, -3.70)) // Madrid

        val resultado = repositorio.findVectoresEnRadio(41.65, -0.87, 10.0)

        assertTrue(resultado.isEmpty())
    }

    // --- Helpers ---

    private fun entidadBase() = EntidadFormularioMonitoreo(
        idForm = null,
        lugar = "Zaragoza",
        vector = "Mosquito",
        enfermedad = "Dengue",
        fecha = LocalDate.of(2024, 4, 1),
        numero = 5,
        genero = 'H',
        latitud = null,
        longitud = null
    )

    private fun entidadConCoordenadas(lat: Double, lon: Double) = EntidadFormularioMonitoreo(
        idForm = null,
        lugar = "Lugar",
        vector = "Mosquito",
        enfermedad = "Dengue",
        fecha = LocalDate.of(2024, 4, 1),
        numero = 5,
        genero = 'H',
        latitud = lat,
        longitud = lon
    )
}