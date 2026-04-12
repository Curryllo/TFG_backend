@file:Suppress("SpellCheckingInspection")
package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.FormularioGarrapatas
import org.unizar.tfg_backend.core.ServicioETL
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioGarrapatas

interface LogFormularioGarrapatasUseCase {
    fun log(f: FormularioGarrapatas): FormularioGarrapatas
}

class LogFormularioGarrapatasUseCaseImpl (
    private val repositorioFormularioGarrapatas: ServicioRepositorioFormularioGarrapatas,
    private val servicioETL: ServicioETL
) : LogFormularioGarrapatasUseCase {
    override fun log(f: FormularioGarrapatas): FormularioGarrapatas {
        repositorioFormularioGarrapatas.save(f)
        servicioETL.ejecutarETL()
        return f
    }
}