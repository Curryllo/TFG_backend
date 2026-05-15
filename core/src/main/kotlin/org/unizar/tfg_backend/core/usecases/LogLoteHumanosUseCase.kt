@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.FormularioHumano
import org.unizar.tfg_backend.core.ServicioETL
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioHumano


interface LogLoteHumanosUseCase {
    fun log(datos: List<FormularioHumano>) : List<FormularioHumano>
}

class LogLoteHumanosUseCaseImpl(
    private val repositorioFormularioHumano: ServicioRepositorioFormularioHumano,
    private val servicioETL: ServicioETL
) : LogLoteHumanosUseCase {
    override fun log(datos: List<FormularioHumano>): List<FormularioHumano> {
        for (dato in datos) {
            repositorioFormularioHumano.save(dato)
        }
        servicioETL.ejecutarETL()
        return datos
    }
}