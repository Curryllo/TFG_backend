package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.ServicioAutenticacion
import org.unizar.tfg_backend.core.TokensDominio


interface InicarSesionUseCase {
    fun iniciarSesion(mail: String, password: String) : TokensDominio
}

class InicarSesionUseCaseImpl (
    private val servicioAutenticacion: ServicioAutenticacion
) : InicarSesionUseCase {
    override fun iniciarSesion(mail: String, password: String) : TokensDominio {
        return servicioAutenticacion.autenticar(mail, password)
    }
}