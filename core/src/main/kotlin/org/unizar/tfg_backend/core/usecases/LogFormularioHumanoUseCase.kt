@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.FormularioHumano
import org.unizar.tfg_backend.core.ServicioETL
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioHumano

interface LogFormularioHumanoUseCase {
    fun log(f: FormularioHumano): FormularioHumano
}

class LogFormularioHumanoUseCaseImpl (
    private val repositorioFormularioHumano: ServicioRepositorioFormularioHumano,
    private val servicioETL: ServicioETL
) : LogFormularioHumanoUseCase {
    override fun log(f: FormularioHumano): FormularioHumano {
        repositorioFormularioHumano.save(f)
        servicioETL.ejecutarETL()
        return f
    }
}