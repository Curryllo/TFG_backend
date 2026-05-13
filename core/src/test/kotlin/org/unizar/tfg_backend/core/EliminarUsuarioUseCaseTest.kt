package org.unizar.tfg_backend.core

import org.mockito.Mockito.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.unizar.tfg_backend.core.usecases.EliminarUsuarioUseCaseImpl
import kotlin.test.Test

class EliminarUsuarioUseCaseTest {
    private val repositorioUsuarios = mock<ServicioRepositorioUsuarios>()
    private val casoDeUso = EliminarUsuarioUseCaseImpl(
        repositorioUsuarios
    )

    @Test
    fun `eliminarUsuario llama al repositorio correctamente`(){
        val email = "test@salud.aragon.es"
        casoDeUso.eliminarUsuario(email)
        verify(repositorioUsuarios, times(1)).eliminarUsuario(email)
    }
}