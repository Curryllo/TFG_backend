@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.FormularioMonitoreo
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioMonitoreo
import java.time.LocalDate

interface LogFormularioMonitoreoUseCase {
    fun log(f: FormularioMonitoreo) : FormularioMonitoreo
}

class LogFormularioMonitoreoUseCaseImpl (
    private val repositorioFormularioMonitoreo: ServicioRepositorioFormularioMonitoreo
) : LogFormularioMonitoreoUseCase {
    override fun log(f: FormularioMonitoreo): FormularioMonitoreo {
        repositorioFormularioMonitoreo.save(f)
        return f
    }
}