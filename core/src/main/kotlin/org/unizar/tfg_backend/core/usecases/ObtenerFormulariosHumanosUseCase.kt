package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.FormularioHumano
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioHumano

interface ObtenerFormulariosHumanosUseCase{
    fun ejecutar(): List<FormularioHumano>
}

class ObtenerFormulariosHumanosUseCaseImpl(
    private val repositorioFormularioHumano: ServicioRepositorioFormularioHumano
): ObtenerFormulariosHumanosUseCase {
    override fun ejecutar(): List<FormularioHumano> {
        return repositorioFormularioHumano.findAll()
    }
}