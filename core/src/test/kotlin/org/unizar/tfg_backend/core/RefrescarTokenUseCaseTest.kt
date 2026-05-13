@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core

import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.unizar.tfg_backend.core.usecases.RefrescarTokenUseCaseImpl

class RefrescarTokenUseCaseTest {
    private val servicioAutenticacion = mock<ServicioAutenticacion>()
    var casoDeUso = RefrescarTokenUseCaseImpl(
        servicioAutenticacion
    )

    @Test
    fun `refrescarTokens llama correctamente al servicio`(){
        val tokensviejos = TokensDominio(
            "token-acceso",
            "token-refresco"
        )

        val tokensnuevos = TokensDominio(
            "nuevo-token-acceso",
            "nuevo-token-refresco"
        )
        `when`(servicioAutenticacion.refrescarTokens(tokensviejos.tokenRefresco)).thenReturn(tokensnuevos)
        casoDeUso.refrescarTokens(tokensviejos.tokenRefresco)
        verify(servicioAutenticacion, times(1))
            .refrescarTokens(tokensviejos.tokenRefresco)
    }
}