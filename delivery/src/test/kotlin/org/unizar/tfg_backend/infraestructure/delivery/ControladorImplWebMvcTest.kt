@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure.delivery

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.unizar.tfg_backend.core.FormularioHumano
import org.unizar.tfg_backend.core.FormularioMonitoreo
import org.unizar.tfg_backend.core.FormularioGarrapatas
import org.unizar.tfg_backend.core.usecases.*
import java.time.LocalDate
import org.mockito.Mockito.anyList

@WebMvcTest(controllers = [ControladorImpl::class])
@AutoConfigureMockMvc(addFilters = false)
class ControladorImplWebMvcTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean lateinit var logFormularioHumanoUseCase: LogFormularioHumanoUseCase
    @MockitoBean lateinit var obtenerFormulariosHumanosUseCase: ObtenerFormulariosHumanosUseCase
    @MockitoBean lateinit var logFormularioMonitoreoUseCase: LogFormularioMonitoreoUseCase
    @MockitoBean lateinit var obtenerFormulariosMonitoreoUseCase: ObtenerFormulariosMonitoreoUseCase
    @MockitoBean lateinit var logFormularioGarrapatasUseCase: LogFormularioGarrapatasUseCase
    @MockitoBean lateinit var obtenerFormulariosGarrapatasUseCase: ObtenerFormulariosGarrapatasUseCase
    @MockitoBean lateinit var descargarArchivoMinIOUseCase: DescargarArchivoMinIOUseCase
    @MockitoBean lateinit var logLoteHumanosUseCase: LogLoteHumanosUseCase
    @MockitoBean lateinit var logLoteGarrapatasUseCase: LogLoteGarrapatasUseCase

    private val mapper = ObjectMapper().registerModule(JavaTimeModule())

    // --- POST /api/formHumanos ---

    @Test
    fun `POST formHumanos devuelve 201 cuando el registro es exitoso`() {
        val formulario = formularioHumanosIn()
        `when`(logFormularioHumanoUseCase.log(formulario.toDomain())).thenReturn(formulario.toDomain())

        mockMvc.perform(
            post("/api/formHumanos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(formulario))
        ).andExpect(status().isCreated)
    }

    // --- GET /api/datosHumanos ---

    @Test
    fun `GET datosHumanos devuelve 200 con lista de formularios`() {
        val lista = listOf(formularioHumanoDominio())
        `when`(obtenerFormulariosHumanosUseCase.ejecutar()).thenReturn(lista)

        mockMvc.perform(get("/api/datosHumanos"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].enfermedad").value("Dengue"))
    }

    @Test
    fun `GET datosHumanos devuelve 204 cuando no hay datos`() {
        `when`(obtenerFormulariosHumanosUseCase.ejecutar()).thenReturn(emptyList())

        mockMvc.perform(get("/api/datosHumanos"))
            .andExpect(status().isNoContent)
    }

    // --- POST /api/formMonitoreo ---

    @Test
    fun `POST formMonitoreo devuelve 201 cuando el registro es exitoso`() {
        val formulario = formularioMonitoreoIn()
        `when`(logFormularioMonitoreoUseCase.log(formulario.toDomain())).thenReturn(formulario.toDomain())

        mockMvc.perform(
            post("/api/formMonitoreo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(formulario))
        ).andExpect(status().isCreated)
    }

    // --- GET /api/datosMonitoreo ---

    @Test
    fun `GET datosMonitoreo devuelve 200 con lista de formularios`() {
        val lista = listOf(formularioMonitoreoDominio())
        `when`(obtenerFormulariosMonitoreoUseCase.ejecutar()).thenReturn(lista)

        mockMvc.perform(get("/api/datosMonitoreo"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].vector").value("Mosquito"))
    }

    @Test
    fun `GET datosMonitoreo devuelve 204 cuando no hay datos`() {
        `when`(obtenerFormulariosMonitoreoUseCase.ejecutar()).thenReturn(emptyList())

        mockMvc.perform(get("/api/datosMonitoreo"))
            .andExpect(status().isNoContent)
    }

    // --- POST /api/formGarrapatas ---

    @Test
    fun `POST formGarrapatas devuelve 201 cuando el registro es exitoso`() {
        val formulario = formularioGarrapatasIn()
        `when`(logFormularioGarrapatasUseCase.log(formulario.toDomain())).thenReturn(formulario.toDomain())

        mockMvc.perform(
            post("/api/formGarrapatas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(formulario))
        ).andExpect(status().isCreated)
    }

    // --- GET /api/datosGarrapatas ---

    @Test
    fun `GET datosGarrapatas devuelve 200 con lista de formularios`() {
        val lista = listOf(formularioGarrapatasDominio())
        `when`(obtenerFormulariosGarrapatasUseCase.ejecutar()).thenReturn(lista)

        mockMvc.perform(get("/api/datosGarrapatas"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].especie").value("Ixodes ricinus"))
    }

    @Test
    fun `GET datosGarrapatas devuelve 204 cuando no hay datos`() {
        `when`(obtenerFormulariosGarrapatasUseCase.ejecutar()).thenReturn(emptyList())

        mockMvc.perform(get("/api/datosGarrapatas"))
            .andExpect(status().isNoContent)
    }

    // --- GET /api/descargaDatos/csv ---

    @Test
    fun `GET descargaDatos devuelve 200 con URL cuando el archivo existe`() {
        `when`(descargarArchivoMinIOUseCase.descargar("tfg-curro-s3", "datos.csv"))
            .thenReturn("http://s3/datos.csv")

        mockMvc.perform(get("/api/descargaDatos/csv").param("archivo", "datos.csv"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.url").value("http://s3/datos.csv"))
    }

    @Test
    fun `GET descargaDatos devuelve 204 cuando el archivo no existe`() {
        `when`(descargarArchivoMinIOUseCase.descargar("tfg-curro-s3", "noexiste.csv"))
            .thenReturn("")

        mockMvc.perform(get("/api/descargaDatos/csv").param("archivo", "noexiste.csv"))
            .andExpect(status().isNoContent)
    }

    // --- POST /api/loteHumanos ---

    @Test
    fun `POST loteHumanos devuelve 200 cuando se guarda un lote no vacio`() {
        val formularios = listOf(formularioHumanosIn(), formularioHumanosIn())
        val dominios = formularios.map { it.toDomain() }
        `when`(logLoteHumanosUseCase.log(anyList())).thenReturn(dominios)

        mockMvc.perform(
            post("/api/loteHumanos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(formularios))
        ).andExpect(status().isOk)
    }

    @Test
    fun `POST loteHumanos devuelve 204 cuando se guarda un lote vacio`() {
        `when`(logLoteHumanosUseCase.log(emptyList())).thenReturn(emptyList())

        mockMvc.perform(
            post("/api/loteHumanos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(emptyList<FormularioHumanosIn>()))
        ).andExpect(status().isNoContent)
    }

// --- POST /api/loteGarrapatas ---

    @Test
    fun `POST loteGarrapatas devuelve 200 cuando se guarda un lote no vacio`() {
        val formularios = listOf(formularioGarrapatasIn(), formularioGarrapatasIn())
        val dominios = formularios.map { it.toDomain() }
        `when`(logLoteGarrapatasUseCase.log(anyList())).thenReturn(dominios)

        mockMvc.perform(
            post("/api/loteGarrapatas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(formularios))
        ).andExpect(status().isOk)
    }

    @Test
    fun `POST loteGarrapatas devuelve 204 cuando se guarda un lote vacio`() {
        `when`(logLoteGarrapatasUseCase.log(emptyList())).thenReturn(emptyList())

        mockMvc.perform(
            post("/api/loteGarrapatas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(emptyList<FormularioGarrapatasIn>()))
        ).andExpect(status().isNoContent)
    }

    // --- Helpers ---

    private fun formularioHumanosIn() = FormularioHumanosIn(
        edad = 35,
        sexo = 'H',
        fechaCaso = LocalDate.of(2024, 4, 1),
        enfermedad = "Dengue",
        pais = "España",
        provinciaResidencia = 'Z',
        municipioResidencia = "Zaragoza",
        defuncion = false,
        casoHospitalizado = false,
        latitud = null,
        longitud = null
    )

    private fun formularioHumanoDominio() = FormularioHumano(
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

    private fun formularioMonitoreoIn() = FormularioMonitoreoIn(
        lugarRecogida = "Zaragoza",
        vector = "Mosquito",
        enfermedad = "Dengue",
        fecha = LocalDate.of(2024, 4, 1),
        numero = 5,
        genero = 'H',
        latitud = null,
        longitud = null
    )

    private fun formularioMonitoreoDominio() = FormularioMonitoreo(
        lugarRecogida = "Zaragoza",
        vector = "Mosquito",
        enfermedad = "Dengue",
        fecha = LocalDate.of(2024, 4, 1),
        numero = 5,
        genero = 'H',
        latitud = null,
        longitud = null
    )

    private fun formularioGarrapatasIn() = FormularioGarrapatasIn(
        municipio = "Zaragoza",
        especie = "Ixodes ricinus",
        fecha = LocalDate.of(2024, 4, 1),
        enHumano = false,
        animal = "Perro",
        latitud = null,
        longitud = null
    )

    private fun formularioGarrapatasDominio() = FormularioGarrapatas(
        municipio = "Zaragoza",
        especie = "Ixodes ricinus",
        fecha = LocalDate.of(2024, 4, 1),
        enHumano = false,
        animal = "Perro",
        latitud = null,
        longitud = null
    )
}