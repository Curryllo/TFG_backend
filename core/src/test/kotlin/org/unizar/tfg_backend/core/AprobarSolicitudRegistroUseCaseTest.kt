@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.times
import org.unizar.tfg_backend.core.usecases.AprobarSolicitudRegistroUseCaseImpl

class AprobarSolicitudRegistroUseCaseTest {
    private val repositorioUsuarios = mock<ServicioRepositorioUsuarios>()
    private val casoDeUso = AprobarSolicitudRegistroUseCaseImpl(
        repositorioUsuarios
    )

    @Test
    fun `aprobarSolicitudRegistro llama al repositorio correctamente`(){
        val email = "test@unizar.es"
        casoDeUso.aprobarSolicitudRegistro(email)
        verify(repositorioUsuarios, times(1)).aprobarSolictudRegistro(email)
    }

}