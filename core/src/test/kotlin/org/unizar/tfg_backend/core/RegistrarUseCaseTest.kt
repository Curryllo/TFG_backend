@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core

import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.unizar.tfg_backend.core.usecases.RegistrarUseCaseImpl

class RegistrarUseCaseTest {
    private val servicioAutenticacion = mock<ServicioAutenticacion>()

    var casoDeUso = RegistrarUseCaseImpl(
        servicioAutenticacion
    )

    @Test
    fun `registrar llama correctamente al servicio`(){
        val usuario = Usuario(
            nombre = "Nomnbre",
            apellido1 =  "Apellido1",
            apellido2 =  "Apellido2",
            puesto = "puesto",
            email = "email",
            rol = "rol",
            password = "password",
            estado = "estado"
        )
        casoDeUso.registrar(usuario)
        verify(servicioAutenticacion, times(1)).registrar(usuario)
    }
}