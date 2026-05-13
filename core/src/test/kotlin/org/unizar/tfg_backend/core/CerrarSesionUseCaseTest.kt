package org.unizar.tfg_backend.core

import org.mockito.Mockito.mock
import org.unizar.tfg_backend.core.usecases.CerrarSesionUseCaseImpl
import kotlin.test.Test
import org.mockito.kotlin.verify
import org.mockito.kotlin.times

class CerrarSesionUseCaseTest {
    private val servicioAutenticacion = mock<ServicioAutenticacion>()
    private val casoDeUso = CerrarSesionUseCaseImpl(
        servicioAutenticacion
    )

    @Test
    fun `cerrarSesion llama al servicio correctamente`(){
        val refreshToken = "tokenDePrueba"
        casoDeUso.cerrarSesion(refreshToken)
        verify(servicioAutenticacion, times(1)).cerrarSesion(refreshToken)
    }
}