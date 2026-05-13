package org.unizar.tfg_backend.core

import org.mockito.Mockito.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.unizar.tfg_backend.core.usecases.DescargarArchivoMinIOUseCaseImpl
import kotlin.test.Test

class DescargarArchivoMinIOUseCaseTest {
    private val servicioMinIO = mock<ServicioMinIO>()
    private val casoDeUso = DescargarArchivoMinIOUseCaseImpl(
        servicioMinIO
    )

    @Test
    fun `descargar llama al servicio correctamente`(){
        casoDeUso.descargar("cubo", "objeto")
        verify(servicioMinIO, times(1)).generarUrlDescarga("cubo", "objeto")
    }
}