@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure.delivery


import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.springframework.http.HttpStatus
import org.unizar.tfg_backend.core.FormularioGarrapatas
import org.unizar.tfg_backend.core.FormularioHumano
import org.unizar.tfg_backend.core.FormularioMonitoreo
import org.unizar.tfg_backend.core.usecases.DescargarArchivoMinIOUseCase
import org.unizar.tfg_backend.core.usecases.LogFormularioGarrapatasUseCase
import org.unizar.tfg_backend.core.usecases.LogFormularioHumanoUseCase
import org.unizar.tfg_backend.core.usecases.LogFormularioMonitoreoUseCase
import org.unizar.tfg_backend.core.usecases.LogLoteGarrapatasUseCase
import org.unizar.tfg_backend.core.usecases.LogLoteHumanosUseCase
import org.unizar.tfg_backend.core.usecases.ObtenerFormulariosGarrapatasUseCase
import org.unizar.tfg_backend.core.usecases.ObtenerFormulariosHumanosUseCase
import org.unizar.tfg_backend.core.usecases.ObtenerFormulariosMonitoreoUseCase
import java.time.LocalDate
import kotlin.test.Test


class ControladorTest {
    /*
    private val logFormularioHumanoUseCase = mock<LogFormularioHumanoUseCase>()
    private val obtenerFormulariosHumanosUseCase = mock<ObtenerFormulariosHumanosUseCase>()
    private val logFormularioMonitoreoUseCase = mock<LogFormularioMonitoreoUseCase>()
    private val obtenerFormulariosMonitoreoUseCase = mock<ObtenerFormulariosMonitoreoUseCase>()
    private val logFormularioGarrapatasUseCase = mock<LogFormularioGarrapatasUseCase>()
    private val obtenerFormulariosGarrapatasUseCase = mock<ObtenerFormulariosGarrapatasUseCase>()
    private val descargarArchivoMinIOUseCase = mock<DescargarArchivoMinIOUseCase>()
    private val logLoteGarrapatasUseCase = mock<LogLoteGarrapatasUseCase>()
    private val logLoteHumanosUseCase = mock<LogLoteHumanosUseCase>()

    private val request = mock<HttpServletRequest>()

    private val controlador = ControladorImpl(
        logFormularioHumanoUseCase,
        obtenerFormulariosHumanosUseCase,
        logFormularioMonitoreoUseCase,
        obtenerFormulariosMonitoreoUseCase,
        logFormularioGarrapatasUseCase,
        obtenerFormulariosGarrapatasUseCase,
        descargarArchivoMinIOUseCase,
        logLoteHumanosUseCase,
        logLoteGarrapatasUseCase
    )

    @Test
    fun `guardarFormularioHumano devuelve 201 cuando el registro tiene éxito`(){
        val formulario = FormularioHumanosIn(
            edad = 22,
            sexo = 'H',
            fechaCaso = LocalDate.now(),
            enfermedad = "Dengue",
            pais = "España",
            provinciaResidencia = 'Z',
            municipioResidencia = "Zaragoza",
            defuncion = false,
            casoHospitalizado = true,
            latitud = null,
            longitud = null
        )
        val dominio = formulario.toDomain()
        `when`(logFormularioHumanoUseCase.log(dominio)).thenReturn(dominio)
        val respuesta = controlador.guardarFormularioHumano(formulario, request)
        assertEquals(HttpStatus.CREATED, respuesta.statusCode)
    }

    @Test
    fun `obtenerDatosHumanos devuelve 200 cuando se obtiene una lista no vacía`(){
        val formulario = FormularioHumano(
            edad = 22,
            sexo = 'H',
            fechaCaso = LocalDate.now(),
            enfermedad = "Dengue",
            pais = "España",
            provinciaResidencia = 'Z',
            municipioResidencia = "Zaragoza",
            defuncion = false,
            hospitalizado = true,
            latitud = null,
            longitud = null
        )
        val lista = listOf(formulario)
        `when`(obtenerFormulariosHumanosUseCase.ejecutar()).thenReturn(lista)
        val respuesta = controlador.obtenerDatosHumanos(request)
        assertEquals(HttpStatus.OK, respuesta.statusCode)
        assertEquals(lista, respuesta.body)
    }

    @Test
    fun `obtenerDatosHumanos devuelve 204 cuando se obtiene una lista vacía`(){
        val lista = emptyList<FormularioHumano>()
        `when`(obtenerFormulariosHumanosUseCase.ejecutar()).thenReturn(lista)
        val respuesta = controlador.obtenerDatosHumanos(request)
        assertEquals(HttpStatus.NO_CONTENT, respuesta.statusCode)
    }

    @Test
    fun `guardarFormularioMonitoreo devuelve 201 cuando el registro tiene éxito`(){
        val formulario = FormularioMonitoreoIn(
            lugarRecogida = "Parque Delicias de Zaragoza",
            vector = "Aedes albopictus",
            fecha = LocalDate.now(),
            numero = 1,
            genero = 'M',
            latitud = null,
            longitud = null,
            enfermedad = null
        )
        val dominio = formulario.toDomain()
        `when`(logFormularioMonitoreoUseCase.log(dominio)).thenReturn(dominio)
        val respuesta = controlador.guardarFormularioMonitoreo(formulario, request)
        assertEquals(HttpStatus.CREATED, respuesta.statusCode)
    }

    @Test
    fun `guardarFormularioMonitoreo devuelve 201 cuando el registro tiene éxito con cooordenadas`(){
        val formulario = FormularioMonitoreoIn(
            lugarRecogida = "",
            vector = "Aedes albopictus",
            fecha = LocalDate.now(),
            numero = 1,
            genero = 'M',
            latitud = 41.6472749,
            longitud = -0.9116654,
            enfermedad = null
        )
        val dominio = formulario.toDomain()
        `when`(logFormularioMonitoreoUseCase.log(dominio)).thenReturn(dominio)
        val respuesta = controlador.guardarFormularioMonitoreo(formulario, request)
        assertEquals(HttpStatus.CREATED, respuesta.statusCode)
    }

    @Test
    fun `obtenerDatosMonitoreo devuelve 200 cuando se obtiene una lista no vacía`(){
        val formularioDominio = FormularioMonitoreo(
            lugarRecogida = "Parque Delicias de Zaragoza",
            vector = "Aedes albopictus",
            fecha = LocalDate.now(),
            numero = 1,
            genero = 'M',
            latitud = null,
            longitud = null,
            enfermedad = null
        )
        val lista = listOf(formularioDominio)
        `when`(obtenerFormulariosMonitoreoUseCase.ejecutar()).thenReturn(lista)
        val respuesta = controlador.obtenerDatosMonitoreo(request)
        assertEquals(HttpStatus.OK, respuesta.statusCode)
        assertEquals(lista, respuesta.body)
    }


    @Test
    fun `obtenerDatosMonitoreo devuelve 204 cuando se obtiene una lista vacía`(){
        val lista = emptyList<FormularioMonitoreo>()
        `when`(obtenerFormulariosMonitoreoUseCase.ejecutar()).thenReturn(lista)
        val respuesta = controlador.obtenerDatosMonitoreo(request)
        assertEquals(HttpStatus.NO_CONTENT, respuesta.statusCode)
    }

    @Test
    fun `guardarFormularioGarrapatas devuelve 201 cuando el registro tiene éxito`(){
        val formulario = FormularioGarrapatasIn(
            municipio = "Zaragoza",
            especie = "marginatus",
            fecha = LocalDate.now(),
            enHumano = false,
            animal = "Ciervo",
            longitud = null,
            latitud = null
        )
        val dominio = formulario.toDomain()
        `when`(logFormularioGarrapatasUseCase.log(dominio)).thenReturn(dominio)
        val respuesta = controlador.guardarFormularioGarrapatas(formulario, request)
        assertEquals(HttpStatus.CREATED, respuesta.statusCode)
    }

    @Test
    fun `obtenerDatosGarrapatas devuelve 200 cuando se obtiene una lista no vacía`(){
        val formulario = FormularioGarrapatas(
            municipio = "Zaragoza",
            especie = "marginatus",
            fecha = LocalDate.now(),
            enHumano = false,
            animal = "Ciervo",
            longitud = null,
            latitud = null
        )
        val lista = listOf(formulario)
        `when`(obtenerFormulariosGarrapatasUseCase.ejecutar()).thenReturn(lista)
        val respuesta = controlador.obtenerDatosGarrapatas(request)
        assertEquals(HttpStatus.OK, respuesta.statusCode)
        assertEquals(lista, respuesta.body)
    }

    @Test
    fun `obtenerDatosGarrapatas devuelve 204 cuando se obtiene una lista vacía`(){
        val lista = emptyList<FormularioGarrapatas>()
        `when`(obtenerFormulariosGarrapatasUseCase.ejecutar()).thenReturn(lista)
        val respuesta = controlador.obtenerDatosGarrapatas(request)
        assertEquals(HttpStatus.NO_CONTENT, respuesta.statusCode)
    }


    @Test
    fun `descargaDatos devuelve 200 cuando se obtiene un link válido`(){
        val urlSimulada: String = "https://minio.midominio.com/tfg-data-lake/datosLimpios.csv\""
        `when`(descargarArchivoMinIOUseCase.descargar("tfg-data-lake", "datosLimpios"))
            .thenReturn(urlSimulada)
        val respuesta = controlador.descargaDatos("datosLimpios")
        assertEquals(HttpStatus.OK, respuesta.statusCode)

        val cuerpoEsperado = mapOf("url" to urlSimulada)
        assertEquals(cuerpoEsperado, respuesta.body)
    }

    @Test
    fun `descargaDatos devuelve 204 cuando se obtiene un link inválido`(){
        val url: String = ""
        `when`(descargarArchivoMinIOUseCase.descargar("tfg-data-lake", "datosLimpios")).thenReturn(url)
        val respuesta = controlador.descargaDatos("datosLimpios")
        assertEquals(HttpStatus.NO_CONTENT, respuesta.statusCode)
        assertNull(respuesta.body)
    }


    @Test
    fun `guardarLoteHumanos devuelve 200 cuando se guarda un lote con municipioResidencia iguales`(){
        val formulario = FormularioHumanosIn(
            edad = 22,
            sexo = 'H',
            fechaCaso = LocalDate.now(),
            enfermedad = "Dengue",
            pais = "España",
            provinciaResidencia = 'Z',
            municipioResidencia = "Zaragoza",
            defuncion = false,
            casoHospitalizado = true,
            latitud = null,
            longitud = null
        )
        val formulario2 = FormularioHumanosIn(
            edad = 22,
            sexo = 'H',
            fechaCaso = LocalDate.now(),
            enfermedad = "Dengue",
            pais = "Marruecos",
            provinciaResidencia = 'Z',
            municipioResidencia = "Zaragoza",
            defuncion = false,
            casoHospitalizado = true,
            latitud = null,
            longitud = null
        )
        val lista = listOf(formulario.toDomain(), formulario2.toDomain())
        `when`(logLoteHumanosUseCase.log(lista)).thenReturn(lista)
        val respuesta = controlador.guardarLoteHumanos(listOf(formulario, formulario2), request)
        assertEquals(HttpStatus.OK, respuesta.statusCode)
        assertEquals(lista, respuesta.body)
    }

    @Test
    fun `guardarLoteHumanos devuelve 204 cuando se guarda un lote vacio`(){
        val lista = emptyList<FormularioHumano>()
        `when`(logLoteHumanosUseCase.log(lista)).thenReturn(lista)
        val respuesta = controlador.guardarLoteHumanos(emptyList<FormularioHumanosIn>(), request)
        assertEquals(HttpStatus.NO_CONTENT, respuesta.statusCode)
    }

    @Test
    fun `guardarLoteGarrapatas devuelve 200 cuando se guarda un lote con municipios iguales`(){
        val formulario = FormularioGarrapatasIn(
            municipio = "Zaragoza",
            especie = "marginatus",
            fecha = LocalDate.now(),
            enHumano = false,
            animal = "Ciervo",
            longitud = null,
            latitud = null
        )
        val formulario2 = FormularioGarrapatasIn(
            municipio = "Zaragoza",
            especie = "marginatus",
            fecha = LocalDate.now(),
            enHumano = false,
            animal = "Corzo",
            longitud = null,
            latitud = null
        )
        val lista = listOf(formulario.toDomain(), formulario2.toDomain())

        `when`(logLoteGarrapatasUseCase.log(lista)).thenReturn(lista)
        val respuesta = controlador.guardarLoteGarrapatas(listOf(formulario, formulario2), request)
        assertEquals(HttpStatus.OK, respuesta.statusCode)
        assertEquals(lista, respuesta.body)
    }

    @Test
    fun `guardarLoteGarrapatas devuelve 204 cuando se guarda un lote vacio`(){
        val lista = emptyList<FormularioGarrapatas>()
        `when`(logLoteGarrapatasUseCase.log(lista)).thenReturn(lista)
        val respuesta = controlador.guardarLoteGarrapatas(emptyList<FormularioGarrapatasIn>(), request)
        assertEquals(HttpStatus.NO_CONTENT, respuesta.statusCode)
    }
    */

}