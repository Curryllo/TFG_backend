@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure.delivery

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.unizar.tfg_backend.core.usecases.LogFormularioHumanoUseCase
import org.unizar.tfg_backend.core.usecases.LogFormularioMonitoreoUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.unizar.tfg_backend.core.usecases.ObtenerFormulariosHumanosUseCase
import java.time.LocalDate

interface Controlador {
    fun guardarFormularioHumano(datos: FormularioHumanosIn, request: HttpServletRequest) : ResponseEntity<Any>
    fun obtenerDatosHumanos(request: HttpServletRequest) : ResponseEntity<Any>
    fun guardarFormularioMonitoreo(datos: FormularioMonitoreoIn, request: HttpServletRequest) : ResponseEntity<Any>
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

@RestController
class ControladorImpl(
    val logFormularioHumanoUseCase: LogFormularioHumanoUseCase,
    val obtenerFormulariosHumanosUseCase: ObtenerFormulariosHumanosUseCase,
    val logFormularioMonitoreoUseCase: LogFormularioMonitoreoUseCase
) : Controlador {
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
        val resultado = logFormularioMonitoreoUseCase.log(
            l = datos.lugarRecogida,
            v = datos.vector,
            e = datos.enfermedad,
            f = datos.fecha,
            n = datos.numero,
            g = datos.genero,
            lat = datos.latitud,
            long = datos.longitud
        )

        return ResponseEntity.status(HttpStatus.CREATED).body(resultado)
    }
}