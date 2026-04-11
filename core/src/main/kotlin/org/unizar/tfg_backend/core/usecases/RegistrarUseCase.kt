package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.ServicioAutenticacion
import org.unizar.tfg_backend.core.Usuario

interface RegistrarUseCase {
    fun registrar(usuario: Usuario)
}

class RegistrarUseCaseImpl (
    private val servicioAutenticacion: ServicioAutenticacion
) : RegistrarUseCase {
    override fun registrar(usuario: Usuario) {
        servicioAutenticacion.registrar(usuario)
    }
}