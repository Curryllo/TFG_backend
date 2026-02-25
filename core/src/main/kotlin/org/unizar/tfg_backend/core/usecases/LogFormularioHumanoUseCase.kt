@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.FormularioHumano
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioHumano
import java.time.LocalDate

interface LogFormularioHumanoUseCase {
    fun log(e: Short, s: Char, mC: Short?, mR: Short?, mD: Short?,
            fIS: LocalDate, d: Boolean, h: Boolean): FormularioHumano
}

class LogFormularioHumanoUseCaseImpl (
    private val repositorioFormularioHumano: ServicioRepositorioFormularioHumano
) : LogFormularioHumanoUseCase {
    override fun log(
        e: Short,
        s: Char,
        mC: Short?,
        mR: Short?,
        mD: Short?,
        fIS: LocalDate,
        d: Boolean,
        h: Boolean
    ): FormularioHumano {
        val form = FormularioHumano(
            edad = e,
            sexo = s,
            municipioCaso = mC,
            municipioResidencia = mR,
            municipioDeclarante = mD,
            fechaInicioSintomas = fIS,
            defuncion = d,
            hospitalizado = h
        )
        repositorioFormularioHumano.save(form)

        return form
    }
}