@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.FormularioMonitoreo
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioMonitoreo
import java.time.LocalDate

interface LogFormularioMonitoreoUseCase {
    fun log(l: String?, v: String, e: String, f: LocalDate, n: Int, g: Char?, lat: Double?, long: Double?) : FormularioMonitoreo
}

class LogFormularioMonitoreoUseCaseImpl (
    private val repositorioFormularioMonitoreo: ServicioRepositorioFormularioMonitoreo
) : LogFormularioMonitoreoUseCase {
    override fun log(
        l: String?,
        v: String,
        e: String,
        f: LocalDate,
        n: Int,
        g: Char?,
        lat: Double?,
        long: Double?): FormularioMonitoreo {
            val form = FormularioMonitoreo(
                l,
                v,
                e,
                f,
                n,
                g,
                lat,
                long,
            )
            repositorioFormularioMonitoreo.save(form)
            return form
    }
}