@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure.repositories

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
open class RepositorioFormularioHumanoJpaTest {

    @Autowired
    lateinit var repositorio: RepositorioFormularioHumanoJpa

    @BeforeEach
    fun limpiarBaseDeDatos() {
        repositorio.deleteAll()
    }

    @Test
    fun `save guarda un formulario y le asigna un id`() {
        val entidad = entidadBase()

        val guardada = repositorio.save(entidad)

        assertNotNull(guardada.idForm)
    }

    @Test
    fun `findAll devuelve todos los formularios guardados`() {
        repositorio.save(entidadBase())
        repositorio.save(EntidadFormularioHumano(
            idForm = null,
            edad = 40,
            sexo = 'M',
            fechaCaso = LocalDate.of(2024, 4, 1),
            enfermedad = "Dengue",
            pais = "España",
            provinciaResidencia = 'H',
            municipioResidencia = "Huesca",
            defuncion = false,
            hospitalizado = false,
            latitud = null,
            longitud = null
        ))

        val resultado = repositorio.findAll()

        assertEquals(2, resultado.size)
    }

    @Test
    fun `findAll devuelve lista vacia cuando no hay formularios`() {
        val resultado = repositorio.findAll()

        assertTrue(resultado.isEmpty())
    }

    @Test
    fun `save persiste correctamente todos los campos`() {
        val entidad = entidadBase()

        val guardada = repositorio.save(entidad)

        assertEquals(35, guardada.edad)
        assertEquals('H', guardada.sexo)
        assertEquals(LocalDate.of(2024, 4, 1), guardada.fechaCaso)
        assertEquals("Dengue", guardada.enfermedad)
        assertEquals("España", guardada.pais)
        assertEquals('Z', guardada.provinciaResidencia)
        assertEquals("Zaragoza", guardada.municipioResidencia)
        assertFalse(guardada.defuncion)
        assertFalse(guardada.hospitalizado)
        assertNull(guardada.latitud)
        assertNull(guardada.longitud)
    }

    @Test
    fun `save persiste correctamente las coordenadas cuando se proporcionan`() {
        val entidad = EntidadFormularioHumano(
            idForm = null,
            edad = 35,
            sexo = 'H',
            fechaCaso = LocalDate.of(2024, 4, 1),
            enfermedad = "Dengue",
            pais = "España",
            provinciaResidencia = 'Z',
            municipioResidencia = "Zaragoza",
            defuncion = false,
            hospitalizado = false,
            latitud = 41.65,
            longitud = -0.87
        )

        val guardada = repositorio.save(entidad)

        assertEquals(41.65, guardada.latitud)
        assertEquals(-0.87, guardada.longitud)
    }

    private fun entidadBase() = EntidadFormularioHumano(
        idForm = null,
        edad = 35,
        sexo = 'H',
        fechaCaso = LocalDate.of(2024, 4, 1),
        enfermedad = "Dengue",
        pais = "España",
        provinciaResidencia = 'Z',
        municipioResidencia = "Zaragoza",
        defuncion = false,
        hospitalizado = false,
        latitud = null,
        longitud = null
    )
}