package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.ServicioAutenticacion
import org.unizar.tfg_backend.core.TokensDominio

interface RefrescarTokenUseCase {
    fun refrescarTokens(refreshToken: String): TokensDominio
}

class RefrescarTokenUseCaseImpl(
    private val servicioAutenticacion: ServicioAutenticacion
) : RefrescarTokenUseCase {
    override fun refrescarTokens(refreshToken: String): TokensDominio {
        return servicioAutenticacion.refrescarTokens(refreshToken)
    }
}
