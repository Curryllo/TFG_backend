package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.ServicioAutenticacion

interface CerrarSesionUseCase {
    fun cerrarSesion(refreshToken: String)
}

class CerrarSesionUseCaseImpl(
    private val servicioAutenticacion: ServicioAutenticacion
) : CerrarSesionUseCase {
    override fun cerrarSesion(refreshToken: String) {
        servicioAutenticacion.cerrarSesion(refreshToken)
    }
}
