@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core


import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.unizar.tfg_backend.core.usecases.LogFormularioMonitoreoUseCaseImpl
import java.time.LocalDate
import kotlin.test.assertEquals

class LogFormularioMonitoreoUseCaseTest {
    private val repositorioFormularioMonitoreo = mock<ServicioRepositorioFormularioMonitoreo>()
    private val servicioETL = org.mockito.kotlin.mock<ServicioETL>()
    private val servicioEmail = org.mockito.kotlin.mock<ServicioEmail>()

    private val casoDeUso = LogFormularioMonitoreoUseCaseImpl(
        repositorioFormularioMonitoreo,
        servicioEmail,
        servicioETL
    )

    @Test
    fun `log guarda el formulario de monitoreo en el repositorio`(){
        val formulario = FormularioMonitoreo(
            lugarRecogida = "Parque Delicias de Zaragoza",
            vector = "Aedes albopictus",
            enfermedad = null,
            fecha = LocalDate.now(),
            numero = 3,
            genero = 'H',
            latitud = null,
            longitud = null,
        )
        `when`(repositorioFormularioMonitoreo.save(formulario)).thenReturn(formulario)
        val resultado = casoDeUso.log(formulario)
        verify(repositorioFormularioMonitoreo, times(1)).save(formulario)
        assertEquals(formulario, resultado)
    }

    @Test
    fun `log ejecuta el ETL siempre tras guardar el formulario`(){
        val formulario = FormularioMonitoreo(
            lugarRecogida = "Parque Delicias de Zaragoza",
            vector = "Aedes albopictus",
            enfermedad = null,
            fecha = LocalDate.now(),
            numero = 3,
            genero = 'H',
            latitud = null,
            longitud = null,
        )
        `when`(repositorioFormularioMonitoreo.save(formulario)).thenReturn(formulario)
        casoDeUso.log(formulario)
        verify(servicioETL, times(1)).ejecutarETL()
    }

    @Test
    fun `log envia mail cuando se registra un vector con una enfermedad`(){
        val formulario = FormularioMonitoreo(
            lugarRecogida = "Parque Delicias de Zaragoza",
            vector = "Aedes albopictus",
            enfermedad = "Fiebre amarilla",
            fecha = LocalDate.now(),
            numero = 3,
            genero = 'H',
            latitud = null,
            longitud = null,
        )

        `when`(repositorioFormularioMonitoreo.save(formulario)).thenReturn(formulario)
        casoDeUso.log(formulario)
        verify(servicioEmail, times(1))
            .sendAlertaVectorInfectado(formulario.enfermedad!!, formulario.lugarRecogida, formulario.vector)
    }
}