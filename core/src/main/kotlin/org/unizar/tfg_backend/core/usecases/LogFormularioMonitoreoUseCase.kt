@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.FormularioMonitoreo
import org.unizar.tfg_backend.core.ServicioETL
import org.unizar.tfg_backend.core.ServicioEmail
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioMonitoreo
import java.time.LocalDate

interface LogFormularioMonitoreoUseCase {
    fun log(f: FormularioMonitoreo) : FormularioMonitoreo
}

class LogFormularioMonitoreoUseCaseImpl (
    private val repositorioFormularioMonitoreo: ServicioRepositorioFormularioMonitoreo,
    private val servicioEmail: ServicioEmail,
    private val servicioETL: ServicioETL
) : LogFormularioMonitoreoUseCase {
    override fun log(f: FormularioMonitoreo): FormularioMonitoreo {
        repositorioFormularioMonitoreo.save(f)
        if (!f.enfermedad.isNullOrBlank()) {
            servicioEmail.sendAlertaVectorInfectado(f.enfermedad, f.lugarRecogida, f.vector)
            println("Email enviado")
        }
        servicioETL.ejecutarETL()
        return f
    }
}