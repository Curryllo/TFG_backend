@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.unizar.tfg_backend.core.usecases.ObtenerFormulariosGarrapatasUseCaseImpl
import kotlin.test.assertEquals

class ObtenerFormulariosGarrapatasUseCaseTest {

    private val servicioRepositorioFormularioGarrapatas = mock<ServicioRepositorioFormularioGarrapatas>()
    private val casoDeUso = ObtenerFormulariosGarrapatasUseCaseImpl(
        servicioRepositorioFormularioGarrapatas
    )

    @Test
    fun `ejecutar llama correctamente al servicio`(){
        val lista = listOf<FormularioGarrapatas>(mock(FormularioGarrapatas::class.java))
        `when`(servicioRepositorioFormularioGarrapatas.findAll()).thenReturn(lista)
        val resultado = casoDeUso.ejecutar()
        verify(servicioRepositorioFormularioGarrapatas, times(1)).findAll()
        assertEquals(lista, resultado)
    }

}