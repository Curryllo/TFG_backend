@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.FormularioGarrapatas
import org.unizar.tfg_backend.core.ServicioETL
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioGarrapatas

interface LogLoteGarrapatasUseCase {
    fun log(datos: List<FormularioGarrapatas>) : List<FormularioGarrapatas>
}

class LogLoteGarrapatasUseCaseImpl (
    private val repositorioFormularioGarrapatas: ServicioRepositorioFormularioGarrapatas,
    private val servicioETL: ServicioETL
) : LogLoteGarrapatasUseCase {
    override fun log(datos: List<FormularioGarrapatas>): List<FormularioGarrapatas> {
        for (dato in datos) {
            repositorioFormularioGarrapatas.save(dato)
        }
        servicioETL.ejecutarETL()
        return datos
    }
}