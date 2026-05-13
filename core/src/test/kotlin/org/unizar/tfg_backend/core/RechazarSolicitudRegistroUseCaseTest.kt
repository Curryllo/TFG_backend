@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.unizar.tfg_backend.core.usecases.RechazarSolicitudesRegistroUseCaseImpl

class RechazarSolicitudRegistroUseCaseTest {
    private val servicioRepositorioUsuarios = mock<ServicioRepositorioUsuarios>()

    private val casoDeUso = RechazarSolicitudesRegistroUseCaseImpl(
        servicioRepositorioUsuarios
    )

    @Test
    fun `rechazarSolicitudesRegistro llama correctamente al servicio`(){
        casoDeUso.rechazarSolicitudesRegistro("mail")
        verify(servicioRepositorioUsuarios, times(1)).rechazarSolicitudRegistro("mail")
    }
}