@file:Suppress("SpellCheckingInspection")
package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.FormularioMonitoreo
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioMonitoreo

interface ObtenerFormulariosMonitoreoUseCase {
    fun ejecutar(): List<FormularioMonitoreo>
}

class ObtenerFormulariosMonitoreoUseCaseImpl(
    private val repositorioFormularioMonitoreo: ServicioRepositorioFormularioMonitoreo
): ObtenerFormulariosMonitoreoUseCase {
    override fun ejecutar(): List<FormularioMonitoreo> {
        return repositorioFormularioMonitoreo.findAll()
    }
}