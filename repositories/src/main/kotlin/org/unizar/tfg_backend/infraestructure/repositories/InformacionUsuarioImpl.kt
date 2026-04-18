package org.unizar.tfg_backend.infraestructure.repositories

import org.unizar.tfg_backend.core.InformacionUsuario
import org.unizar.tfg_backend.core.Usuario

class InformacionUsuarioImpl (
    private val repositorioUsuariosJpa: RepositorioUsuariosJpa
) : InformacionUsuario {
    override fun autenticar(email: String, password: String)  : Usuario {
        val usuario = repositorioUsuariosJpa.findByEmail(email)
        return Usuario(
            usuario?.nombre ?: "",
            usuario?.apellido1 ?: "",
            usuario?.apellido2 ?: "",
            usuario?.puesto ?: "",
            usuario?.email ?: "",
            usuario?.rol ?: "",
            usuario?.password ?: "",
            usuario?.estado ?: ""
        )
    }
}