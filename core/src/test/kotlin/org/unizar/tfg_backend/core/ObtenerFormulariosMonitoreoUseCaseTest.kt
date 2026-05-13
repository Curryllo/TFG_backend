@file:Suppress("SpellCheckingInspection")
package org.unizar.tfg_backend.core

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.unizar.tfg_backend.core.usecases.ObtenerFormulariosMonitoreoUseCaseImpl
import kotlin.test.assertEquals

class ObtenerFormulariosMonitoreoUseCaseTest {

    private val servicioRepositorioFormularioMonitoreo = mock<ServicioRepositorioFormularioMonitoreo>()
    private val casoDeUso = ObtenerFormulariosMonitoreoUseCaseImpl(
        servicioRepositorioFormularioMonitoreo
    )

    @Test
    fun `ejecutar llama correctamente al servicio`(){
        val lista = listOf<FormularioMonitoreo>(mock(FormularioMonitoreo::class.java))
        `when`(servicioRepositorioFormularioMonitoreo.findAll()).thenReturn(lista)
        val resultado = casoDeUso.ejecutar()
        verify(servicioRepositorioFormularioMonitoreo, times(1)).findAll()
        assertEquals(lista, resultado)
    }
}