@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.FormularioHumano
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioHumano
import java.time.LocalDate

interface LogFormularioHumanoUseCase {
    fun log(ed: Short, s: Char, fC: LocalDate, en: String, p: String,
            pR: Char, mR: String, d: Boolean, h: Boolean): FormularioHumano
}

class LogFormularioHumanoUseCaseImpl (
    private val repositorioFormularioHumano: ServicioRepositorioFormularioHumano
) : LogFormularioHumanoUseCase {
    override fun log(
        ed: Short,
        s: Char,
        fC: LocalDate,
        en: String,
        p: String,
        pR: Char,
        mR: String,
        d: Boolean,
        h: Boolean
    ): FormularioHumano {
        val form = FormularioHumano(
            edad = ed,
            sexo = s,
            fechaCaso = fC,
            enfermedad = en,
            pais = p,
            provinciaResidencia = pR,
            municipioResidencia = mR,
            defuncion = d,
            hospitalizado = h
        )
        repositorioFormularioHumano.save(form)

        return form
    }
}