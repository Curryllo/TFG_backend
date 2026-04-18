package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.ServicioRepositorioUsuarios
import org.unizar.tfg_backend.core.Usuario

interface ObtenerSolicitudesRegistroUseCase{
    fun obtenerSolicitudesRegistro() : List<Usuario>
}

class ObtenerSolicitudesRegistroUseCaseImpl(
    private val servicioRepositorioUsuarios: ServicioRepositorioUsuarios
) : ObtenerSolicitudesRegistroUseCase {
    override fun obtenerSolicitudesRegistro() : List<Usuario> {
        return servicioRepositorioUsuarios.listadoSolicitudesRegistro()
    }
}