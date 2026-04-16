@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.FormularioHumano
import org.unizar.tfg_backend.core.ServicioETL
import org.unizar.tfg_backend.core.ServicioEmail
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioHumano
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioMonitoreo
import java.lang.System.console

interface LogFormularioHumanoUseCase {
    fun log(f: FormularioHumano): FormularioHumano
}

class LogFormularioHumanoUseCaseImpl(
        private val repositorioFormularioHumano: ServicioRepositorioFormularioHumano,
        private val servicioETL: ServicioETL,
        private val repositorioFormularioMonitoreo: ServicioRepositorioFormularioMonitoreo,
        private val servicioEmail: ServicioEmail,
        private val radioAlertaKm: Double = 10.0
) : LogFormularioHumanoUseCase {
    override fun log(f: FormularioHumano): FormularioHumano {
        repositorioFormularioHumano.save(f)

        if (f.latitud != null && f.longitud != null) {
            val vectoresCercanos =
                    repositorioFormularioMonitoreo.buscarVectoresEnRadio(f.latitud, f.longitud, radioAlertaKm
                    )
            if (vectoresCercanos.isNotEmpty()) {
                println("Correo de alerta de caso humano cercano a donde hay vectores")
                servicioEmail.sendAlertaCasoHumanoCercaVectores(f.enfermedad, f.municipioResidencia, vectoresCercanos)
                println("Email de alerta de proximidad enviado")
            }
        }

        servicioETL.ejecutarETL()
        return f
    }
}
