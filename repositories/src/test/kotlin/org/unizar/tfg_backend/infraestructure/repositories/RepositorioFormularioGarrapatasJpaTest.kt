@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure.repositories

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
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
open class RepositorioFormularioGarrapatasJpaTest {

    @Autowired
    lateinit var repositorio: RepositorioFormularioGarrapatasJpa

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
        repositorio.save(EntidadFormularioGarrapata(
            idForm = null,
            municipioRecogida = "Huesca",
            especie = "marginatus",
            fechaRecogida = LocalDate.of(2024, 4, 1),
            enHumano = true,
            animal = "",
            longitud = null,
            latitud = null))

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

        assertEquals("Zaragoza", guardada.municipioRecogida)
        assertEquals("punctata", guardada.especie)
        assertEquals(LocalDate.of(2024, 4, 1), guardada.fechaRecogida)
        assertFalse(guardada.enHumano)
        assertEquals("Corzo", guardada.animal)
        assertNull(guardada.latitud)
        assertNull(guardada.longitud)

    }

    @Test
    fun `save persiste correctamente las coordenadas cuando se proporcionan`() {
        val entidad = EntidadFormularioGarrapata(
            idForm = null,
            municipioRecogida = "Zaragoza",
            especie = "punctata",
            fechaRecogida = LocalDate.of(2024, 4, 1),
            enHumano = false,
            animal = "Corzo",
            latitud = 41.65,
            longitud = -0.87
        )

        val guardada = repositorio.save(entidad)

        assertEquals(41.65, guardada.latitud)
        assertEquals(-0.87, guardada.longitud)
    }

    private fun entidadBase() = EntidadFormularioGarrapata(
        idForm = null,
        municipioRecogida = "Zaragoza",
        especie = "punctata",
        fechaRecogida = LocalDate.of(2024, 4, 1),
        enHumano = false,
        animal = "Corzo",
        longitud = null,
        latitud = null
    )
}