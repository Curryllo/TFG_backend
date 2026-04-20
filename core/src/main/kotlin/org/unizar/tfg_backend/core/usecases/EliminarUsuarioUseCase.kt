package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.ServicioRepositorioUsuarios

interface EliminarUsuarioUseCase {
    fun eliminarUsuario(email: String)
}

class EliminarUsuarioUseCaseImpl(
    private val servicioRepositorioUsuarios: ServicioRepositorioUsuarios
) : EliminarUsuarioUseCase {
    override fun eliminarUsuario(email: String) {
        servicioRepositorioUsuarios.eliminarUsuario(email)
    }
}