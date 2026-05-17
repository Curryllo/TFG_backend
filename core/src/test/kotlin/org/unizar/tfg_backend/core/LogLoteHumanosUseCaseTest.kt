@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core

import org.mockito.Mockito
import kotlin.test.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.unizar.tfg_backend.core.usecases.LogLoteHumanosUseCaseImpl
import kotlin.test.assertEquals
class LogLoteHumanosUseCaseTest {
    private val repositorioFormularioHumano = mock<ServicioRepositorioFormularioHumano>()
    private val servicioETL = mock<ServicioETL>()

    private val casoDeUso = LogLoteHumanosUseCaseImpl(
        repositorioFormularioHumano,
        servicioETL
    )

    @Test
    fun `log llama correctamente al servicio`(){
        val humano1 = Mockito.mock(FormularioHumano::class.java)
        val humano2 = Mockito.mock(FormularioHumano::class.java)
        val datos = listOf(humano1, humano2)
        `when`(repositorioFormularioHumano.save(humano1)).thenReturn(humano1)
        `when`(repositorioFormularioHumano.save(humano2)).thenReturn(humano2)
        val resultado = casoDeUso.log(datos)
        assertEquals(resultado, datos)
    }
}