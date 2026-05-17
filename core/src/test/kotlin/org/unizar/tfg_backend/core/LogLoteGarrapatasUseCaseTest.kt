@file:Suppress("SpellCheckingInspection")
package org.unizar.tfg_backend.core

import org.mockito.Mockito.mock
import org.unizar.tfg_backend.core.usecases.LogLoteGarrapatasUseCaseImpl
import kotlin.test.Test
import org.mockito.Mockito.`when`
import kotlin.test.assertEquals

class LogLoteGarrapatasUseCaseTest {
    private val repositorioFormularioGarrapatas = mock<ServicioRepositorioFormularioGarrapatas>()
    private val servicioETL = mock<ServicioETL>()

    private val casoDeUso = LogLoteGarrapatasUseCaseImpl(
        repositorioFormularioGarrapatas,
        servicioETL
    )

    @Test
    fun `log llama correctamente al servicio`(){
        val garrapata1 = mock(FormularioGarrapatas::class.java)
        val garrapata2 = mock(FormularioGarrapatas::class.java)
        val datos = listOf(garrapata1, garrapata2)
        `when`(repositorioFormularioGarrapatas.save(garrapata1)).thenReturn(garrapata1)
        `when`(repositorioFormularioGarrapatas.save(garrapata2)).thenReturn(garrapata2)
        val resultado = casoDeUso.log(datos)
        assertEquals(resultado, datos)
    }
}