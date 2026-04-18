package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.ServicioRepositorioUsuarios

interface RechazarSolicitudesRegistroUseCase{
    fun rechazarSolicitudesRegistro(email: String)
}

class RechazarSolicitudesRegistroUseCaseImpl(
    private val servicioRepositorioUsuarios: ServicioRepositorioUsuarios
) : RechazarSolicitudesRegistroUseCase {
    override fun rechazarSolicitudesRegistro(email: String) {
        servicioRepositorioUsuarios.rechazarSolicitudRegistro(email)
    }
}