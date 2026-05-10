package org.unizar.tfg_backend.infraestructure.repositories

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.unizar.tfg_backend.core.FormularioGarrapatas
import org.unizar.tfg_backend.core.FormularioHumano
import org.unizar.tfg_backend.core.FormularioMonitoreo
import org.unizar.tfg_backend.core.Usuario
import java.time.LocalDate

class ConvertidoresKtTest {

    // --- FormularioHumano ---

    @Test
    fun `FormularioHumano toEntity convierte todos los campos correctamente`() {
        val dominio = formularioHumano()
        val entidad = dominio.toEntity()

        assertNull(entidad.idForm)
        assertEquals(dominio.edad, entidad.edad)
        assertEquals(dominio.sexo, entidad.sexo)
        assertEquals(dominio.fechaCaso, entidad.fechaCaso)
        assertEquals(dominio.enfermedad, entidad.enfermedad)
        assertEquals(dominio.pais, entidad.pais)
        assertEquals(dominio.provinciaResidencia, entidad.provinciaResidencia)
        assertEquals(dominio.municipioResidencia, entidad.municipioResidencia)
        assertEquals(dominio.defuncion, entidad.defuncion)
        assertEquals(dominio.hospitalizado, entidad.hospitalizado)
        assertEquals(dominio.latitud, entidad.latitud)
        assertEquals(dominio.longitud, entidad.longitud)
    }

    @Test
    fun `EntidadFormularioHumano toDomain convierte todos los campos correctamente`() {
        val entidad = EntidadFormularioHumano(
            idForm = 1,
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
        val dominio = entidad.toDomain()

        assertEquals(entidad.edad, dominio.edad)
        assertEquals(entidad.sexo, dominio.sexo)
        assertEquals(entidad.fechaCaso, dominio.fechaCaso)
        assertEquals(entidad.enfermedad, dominio.enfermedad)
        assertEquals(entidad.pais, dominio.pais)
        assertEquals(entidad.provinciaResidencia, dominio.provinciaResidencia)
        assertEquals(entidad.municipioResidencia, dominio.municipioResidencia)
        assertEquals(entidad.defuncion, dominio.defuncion)
        assertEquals(entidad.hospitalizado, dominio.hospitalizado)
        assertEquals(entidad.latitud, dominio.latitud)
        assertEquals(entidad.longitud, dominio.longitud)
    }

    @Test
    fun `FormularioHumano toEntity y toDomain son inversas`() {
        val original = formularioHumano()
        val resultado = original.toEntity().toDomain()
        assertEquals(original, resultado)
    }

    // --- FormularioMonitoreo ---

    @Test
    fun `FormularioMonitoreo toEntity convierte todos los campos correctamente`() {
        val dominio = formularioMonitoreo()
        val entidad = dominio.toEntity()

        assertNull(entidad.idForm)
        assertEquals(dominio.lugarRecogida, entidad.lugar)
        assertEquals(dominio.vector, entidad.vector)
        assertEquals(dominio.enfermedad, entidad.enfermedad)
        assertEquals(dominio.fecha, entidad.fecha)
        assertEquals(dominio.numero, entidad.numero)
        assertEquals(dominio.genero, entidad.genero)
        assertEquals(dominio.latitud, entidad.latitud)
        assertEquals(dominio.longitud, entidad.longitud)
    }

    @Test
    fun `EntidadFormularioMonitoreo toDomain convierte todos los campos correctamente`() {
        val entidad = EntidadFormularioMonitoreo(
            idForm = 1,
            lugar = "Zaragoza",
            vector = "Mosquito",
            enfermedad = "Dengue",
            fecha = LocalDate.of(2024, 4, 1),
            numero = 5,
            genero = 'H',
            latitud = 41.65,
            longitud = -0.87
        )
        val dominio = entidad.toDomain()

        assertEquals(entidad.lugar, dominio.lugarRecogida)
        assertEquals(entidad.vector, dominio.vector)
        assertEquals(entidad.enfermedad, dominio.enfermedad)
        assertEquals(entidad.fecha, dominio.fecha)
        assertEquals(entidad.numero, dominio.numero)
        assertEquals(entidad.genero, dominio.genero)
        assertEquals(entidad.latitud, dominio.latitud)
        assertEquals(entidad.longitud, dominio.longitud)
    }

    @Test
    fun `FormularioMonitoreo toEntity y toDomain son inversas`() {
        val original = formularioMonitoreo()
        val resultado = original.toEntity().toDomain()
        assertEquals(original, resultado)
    }

    // --- FormularioGarrapatas ---

    @Test
    fun `FormularioGarrapatas toEntity convierte todos los campos correctamente`() {
        val dominio = formularioGarrapatas()
        val entidad = dominio.toEntity()

        assertNull(entidad.idForm)
        assertEquals(dominio.municipio, entidad.municipioRecogida)
        assertEquals(dominio.especie, entidad.especie)
        assertEquals(dominio.fecha, entidad.fechaRecogida)
        assertEquals(dominio.enHumano, entidad.enHumano)
        assertEquals(dominio.animal, entidad.animal)
        assertEquals(dominio.latitud, entidad.latitud)
        assertEquals(dominio.longitud, entidad.longitud)
    }

    @Test
    fun `EntidadFormularioGarrapata toDomain convierte todos los campos correctamente`() {
        val entidad = EntidadFormularioGarrapata(
            idForm = 1,
            municipioRecogida = "Zaragoza",
            especie = "Ixodes ricinus",
            fechaRecogida = LocalDate.of(2024, 4, 1),
            enHumano = true,
            animal = "Perro",
            latitud = 41.65,
            longitud = -0.87
        )
        val dominio = entidad.toDomain()

        assertEquals(entidad.municipioRecogida, dominio.municipio)
        assertEquals(entidad.especie, dominio.especie)
        assertEquals(entidad.fechaRecogida, dominio.fecha)
        assertEquals(entidad.enHumano, dominio.enHumano)
        assertEquals(entidad.animal, dominio.animal)
        assertEquals(entidad.latitud, dominio.latitud)
        assertEquals(entidad.longitud, dominio.longitud)
    }

    @Test
    fun `FormularioGarrapatas toEntity y toDomain son inversas`() {
        val original = formularioGarrapatas()
        val resultado = original.toEntity().toDomain()
        assertEquals(original, resultado)
    }

    // --- Usuario ---

    @Test
    fun `Usuario toEntity convierte todos los campos correctamente`() {
        val dominio = usuario()
        val entidad = dominio.toEntity()

        assertNull(entidad.idUsuario)
        assertEquals(dominio.nombre, entidad.nombre)
        assertEquals(dominio.apellido1, entidad.apellido1)
        assertEquals(dominio.apellido2, entidad.apellido2)
        assertEquals(dominio.puesto, entidad.puesto)
        assertEquals(dominio.email, entidad.email)
        assertEquals(dominio.rol, entidad.rol)
        assertEquals(dominio.password, entidad.password)
        assertEquals(dominio.estado, entidad.estado)
    }

    @Test
    fun `EntidadUsuario toDomain convierte todos los campos correctamente`() {
        val entidad = EntidadUsuario(
            idUsuario = 1,
            nombre = "Juan",
            apellido1 = "García",
            apellido2 = "López",
            puesto = "Médico",
            email = "juan@test.com",
            rol = "USER",
            password = "password123",
            estado = "Activo"
        )
        val dominio = entidad.toDomain()

        assertEquals(entidad.nombre, dominio.nombre)
        assertEquals(entidad.apellido1, dominio.apellido1)
        assertEquals(entidad.apellido2, dominio.apellido2)
        assertEquals(entidad.puesto, dominio.puesto)
        assertEquals(entidad.email, dominio.email)
        assertEquals(entidad.rol, dominio.rol)
        assertEquals(entidad.password, dominio.password)
        assertEquals(entidad.estado, dominio.estado)
    }

    @Test
    fun `Usuario toEntity y toDomain son inversas`() {
        val original = usuario()
        val resultado = original.toEntity().toDomain()
        assertEquals(original, resultado)
    }

    // --- Helpers ---

    private fun formularioHumano() = FormularioHumano(
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

    private fun formularioMonitoreo() = FormularioMonitoreo(
        lugarRecogida = "Zaragoza",
        vector = "Mosquito",
        enfermedad = "Dengue",
        fecha = LocalDate.of(2024, 4, 1),
        numero = 5,
        genero = 'H',
        latitud = 41.65,
        longitud = -0.87
    )

    private fun formularioGarrapatas() = FormularioGarrapatas(
        municipio = "Zaragoza",
        especie = "Ixodes ricinus",
        fecha = LocalDate.of(2024, 4, 1),
        enHumano = true,
        animal = "Perro",
        latitud = 41.65,
        longitud = -0.87
    )

    private fun usuario() = Usuario(
        nombre = "Juan",
        apellido1 = "García",
        apellido2 = "López",
        puesto = "Médico",
        email = "juan@test.com",
        rol = "USER",
        password = "password123",
        estado = "Activo"
    )
}