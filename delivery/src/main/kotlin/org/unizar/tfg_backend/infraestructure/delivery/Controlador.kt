@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure.delivery

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.unizar.tfg_backend.core.usecases.LogFormularioHumanoUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import java.time.LocalDate

interface Controlador {
    fun guardarFormularioHumano(datos: FormularioHumanosIn, request: HttpServletRequest) : ResponseEntity<Any>
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

@RestController
class ControladorImpl(
    val logFormularioHumanoUseCase: LogFormularioHumanoUseCase
) : Controlador {
    @PostMapping(value = ["/api/formHumanos"])
    override fun guardarFormularioHumano(
        @RequestBody datos: FormularioHumanosIn, request: HttpServletRequest): ResponseEntity<Any> {
        println("Ha entrado")
        println("Edad: " + datos.edad)
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
}