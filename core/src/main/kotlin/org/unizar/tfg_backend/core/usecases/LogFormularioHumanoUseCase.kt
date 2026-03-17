@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.FormularioHumano
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioHumano
import java.time.LocalDate

interface LogFormularioHumanoUseCase {
    fun log(f: FormularioHumano): FormularioHumano
}

class LogFormularioHumanoUseCaseImpl (
    private val repositorioFormularioHumano: ServicioRepositorioFormularioHumano
) : LogFormularioHumanoUseCase {
    override fun log(f: FormularioHumano): FormularioHumano {
        repositorioFormularioHumano.save(f)
        return f
    }
}