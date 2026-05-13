@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.unizar.tfg_backend.core.usecases.ObtenerFormulariosHumanosUseCaseImpl
import kotlin.test.assertEquals

class ObtenerFormulariosHumanosUseCaseTest {
    private val servicioRepositorioFormularioHumano = mock<ServicioRepositorioFormularioHumano>()
    private val casoDeUso = ObtenerFormulariosHumanosUseCaseImpl(
        servicioRepositorioFormularioHumano
    )

    @Test
    fun `ejecutar llama correctamente al servicio`(){
        val lista = listOf<FormularioHumano>(mock(FormularioHumano::class.java))
        `when`(servicioRepositorioFormularioHumano.findAll()).thenReturn(lista)
        val resultado = casoDeUso.ejecutar()
        verify(servicioRepositorioFormularioHumano, times(1)).findAll()
        assertEquals(lista, resultado)
    }
}