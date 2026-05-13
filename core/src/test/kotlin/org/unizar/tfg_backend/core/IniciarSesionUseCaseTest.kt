@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core

import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.unizar.tfg_backend.core.usecases.InicarSesionUseCaseImpl
import kotlin.test.Test
import kotlin.test.assertEquals

class IniciarSesionUseCaseTest {
    private val servicioAutenticacion = mock<ServicioAutenticacion>()
    private val casoDeUso = InicarSesionUseCaseImpl(
        servicioAutenticacion
    )

    @Test
    fun `iniciarSesion llama correctamente al servicio`(){
        val tokens = TokensDominio(
            "token-acceso",
            "token-refresco"
        )
        `when`(servicioAutenticacion.autenticar("mail", "password")).thenReturn(tokens)
        val resultado = casoDeUso.iniciarSesion("mail", "password")
        verify(servicioAutenticacion, times(1))
            .autenticar("mail", "password")
        assertEquals(tokens, resultado)
    }
}