@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure.delivery

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.unizar.tfg_backend.core.usecases.LogFormularioHumanoUseCase
import org.unizar.tfg_backend.core.usecases.LogFormularioMonitoreoUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import org.unizar.tfg_backend.core.usecases.ObtenerFormulariosHumanosUseCase
import java.time.LocalDate

interface Controlador {
    fun guardarFormularioHumano(datos: FormularioHumanosIn, request: HttpServletRequest) : ResponseEntity<Any>
    fun obtenerDatosHumanos(request: HttpServletRequest) : ResponseEntity<Any>
    fun guardarFormularioMonitoreo(datos: FormularioMonitoreoIn, request: HttpServletRequest) : ResponseEntity<Any>
    fun completarDatosGeograficos(datos: FormularioMonitoreoIn) : FormularioMonitoreoIn
}


data class FormularioHumanosIn (
    val edad: Short = 0,
    val sexo: Char = 'U',
    val fechaInicioSintomas: LocalDate = LocalDate.now(),
    val municipioCaso: Short? = null,
    val municipioResidencia: Short? = null,
    val municipioDeclarante: Short? = null,
    val defuncion: Boolean = false,
    val casoHospitalizado: Boolean = false
)

data class FormularioMonitoreoIn(
    val lugarRecogida: String? = null,
    val vector: String = "",
    val enfermedad: String = "",
    val fecha: LocalDate = LocalDate.now(),
    val numero: Int = 1,
    val genero: Char? = 'M',
    val latitud: Double? = null,
    val longitud: Double? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NominatimSearchResponse(
    val lat: String = "",
    val lon: String = ""
)

data class NominatimReverseResponse(
    @JsonProperty("display_name")
    val displayName: String = ""
)

@RestController
class ControladorImpl(
    val logFormularioHumanoUseCase: LogFormularioHumanoUseCase,
    val obtenerFormulariosHumanosUseCase: ObtenerFormulariosHumanosUseCase,
    val logFormularioMonitoreoUseCase: LogFormularioMonitoreoUseCase
) : Controlador {
    private val restTemplate = RestTemplate()
    private val nominatimUrl = "https://nominatim.openstreetmap.org"

    override fun completarDatosGeograficos(datos: FormularioMonitoreoIn): FormularioMonitoreoIn {
        val headers = HttpHeaders()
        headers.set("User-Agent", "TFG_IngInfor_Monitoreo")
        val entity = HttpEntity<String>(headers)

        if (!datos.lugarRecogida.isNullOrBlank() && datos.latitud == null) {
            val url = UriComponentsBuilder.fromHttpUrl("$nominatimUrl/search")
                .queryParam("q", datos.lugarRecogida)
                .queryParam("format", "json")
                .queryParam("limit", 1)
                .build().toUriString()

            val response = restTemplate.exchange(url, HttpMethod.GET, entity, Array<NominatimSearchResponse>::class.java)
            val body = response.body
            if (!body.isNullOrEmpty()) {
                return datos.copy(
                    latitud = body[0].lat.toDouble(),
                    longitud = body[0].lon.toDouble()
                )
            }
        }

        if (datos.latitud != null && datos.longitud != null && datos.lugarRecogida.isNullOrBlank()) {
            val url = UriComponentsBuilder.fromHttpUrl("$nominatimUrl/reverse")
                .queryParam("lat", datos.latitud)
                .queryParam("lon", datos.longitud)
                .queryParam("format", "json")
                .build().toUriString()

            val response = restTemplate.exchange(url, HttpMethod.GET, entity, NominatimReverseResponse::class.java)

            response.body?.let {
                return datos.copy(lugarRecogida = it.displayName)
            }
        }
        return datos
    }
    @PostMapping(value = ["/api/formHumanos"])
    override fun guardarFormularioHumano(
        @RequestBody datos: FormularioHumanosIn, request: HttpServletRequest): ResponseEntity<Any> {
        //println("Ha entrado")
        //println("Edad: " + datos.edad)
        val resultado = logFormularioHumanoUseCase.log(
            e = datos.edad,
            s = datos.sexo,
            mC = datos.municipioCaso,
            mR = datos.municipioResidencia,
            mD = datos.municipioDeclarante,
            fIS = datos.fechaInicioSintomas,
            d = datos.defuncion,
            h = datos.casoHospitalizado
        )

        return ResponseEntity.status(HttpStatus.CREATED).body(resultado)
    }

    @GetMapping(value = ["/api/datosHumanos"])
    override fun obtenerDatosHumanos(request: HttpServletRequest): ResponseEntity<Any> {
        val lista = obtenerFormulariosHumanosUseCase.ejecutar()

        return if (lista.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(lista)
        }
    }


    @PostMapping(value = ["/api/formMonitoreo"])
    override fun guardarFormularioMonitoreo(
        @RequestBody datos: FormularioMonitoreoIn, request: HttpServletRequest): ResponseEntity<Any> {
        println("Ha entrado")
        val datosCompletos = completarDatosGeograficos(datos)

        val resultado = logFormularioMonitoreoUseCase.log(
            l = datosCompletos.lugarRecogida,
            v = datosCompletos.vector,
            e = datosCompletos.enfermedad,
            f = datosCompletos.fecha,
            n = datosCompletos.numero,
            g = datosCompletos.genero,
            lat = datosCompletos.latitud,
            long = datosCompletos.longitud
        )

        return ResponseEntity.status(HttpStatus.CREATED).body(resultado)
    }
    

}