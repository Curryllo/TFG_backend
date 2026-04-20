package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.ServicioRepositorioUsuarios
import org.unizar.tfg_backend.core.Usuario

interface ObtenerUsuariosActivosUseCase {
    fun obtenerUsuariosActivos() : List<Usuario>
}

class ObtenerUsuariosActivosImpl(
    private val servicioRepositorioUsuarios: ServicioRepositorioUsuarios
) : ObtenerUsuariosActivosUseCase {
    override fun obtenerUsuariosActivos(): List<Usuario> {
       return servicioRepositorioUsuarios.listadoUsuariosActivos()
    }
}

