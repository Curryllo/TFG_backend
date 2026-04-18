package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.ServicioRepositorioUsuarios

interface AprobarSolicitudRegistroUseCase {
    fun aprobarSolicitudRegistro(email: String)
}

class AprobarSolicitudRegistroUseCaseImpl(
    private val servicioRepositorioUsuarios: ServicioRepositorioUsuarios
) : AprobarSolicitudRegistroUseCase {
    override fun aprobarSolicitudRegistro(email: String) {
        servicioRepositorioUsuarios.aprobarSolictudRegistro(email)
    }
}