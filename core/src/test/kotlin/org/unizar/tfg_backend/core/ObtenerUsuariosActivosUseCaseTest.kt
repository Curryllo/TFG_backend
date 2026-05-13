@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core

import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.unizar.tfg_backend.core.usecases.ObtenerUsuariosActivosImpl
import kotlin.test.assertEquals
import kotlin.test.Test

class ObtenerUsuariosActivosUseCaseTest {
    private val servicioRepositorioUsuarios= mock<ServicioRepositorioUsuarios>()
    private val casoDeUso = ObtenerUsuariosActivosImpl(
        servicioRepositorioUsuarios
    )

    @Test
    fun `obtenerSolicitudesRegistro llama correctamente al servicio`(){
        val listaEsperada = listOf(mock(Usuario::class.java), mock(Usuario::class.java))
        `when`(servicioRepositorioUsuarios.listadoUsuariosActivos()).thenReturn(listaEsperada)
        val resultado = casoDeUso.obtenerUsuariosActivos()
        verify(servicioRepositorioUsuarios, times(1)).listadoUsuariosActivos()
        assertEquals(listaEsperada, resultado)
    }
}