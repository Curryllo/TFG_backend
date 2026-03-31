@file:Suppress("SpellCheckingInspection")
package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.FormularioGarrapatas
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioGarrapatas


interface ObtenerFormulariosGarrapatasUseCase {
    fun ejecutar(): List<FormularioGarrapatas>
}

class ObtenerFormulariosGarrapatasUseCaseImpl (
    private val repositorioFormularioGarrapatas: ServicioRepositorioFormularioGarrapatas
) : ObtenerFormulariosGarrapatasUseCase {
    override fun ejecutar(): List<FormularioGarrapatas> {
        return repositorioFormularioGarrapatas.findAll()
    }
}