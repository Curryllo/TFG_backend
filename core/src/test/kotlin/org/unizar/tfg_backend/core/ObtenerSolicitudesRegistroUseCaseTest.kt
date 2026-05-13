@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core

import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.unizar.tfg_backend.core.usecases.ObtenerSolicitudesRegistroUseCaseImpl
import kotlin.test.Test
import kotlin.test.assertEquals

class ObtenerSolicitudesRegistroUseCaseTest {
    private val servicioRepositorioUsuarios = mock<ServicioRepositorioUsuarios>()

    private val casoDeUso = ObtenerSolicitudesRegistroUseCaseImpl(
        servicioRepositorioUsuarios
    )

    @Test
    fun `obtenerSolicitudesRegistro llama correctamente al servicio`(){
        val listaEsperada = listOf(mock(Usuario::class.java), mock(Usuario::class.java))
        `when`(servicioRepositorioUsuarios.listadoSolicitudesRegistro()).thenReturn(listaEsperada)
        val resultado = casoDeUso.obtenerSolicitudesRegistro()
        verify(servicioRepositorioUsuarios, times(1)).listadoSolicitudesRegistro()
        assertEquals(listaEsperada, resultado)
    }
}