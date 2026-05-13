@file:Suppress("SpellCheckingInspection")
package org.unizar.tfg_backend.core

import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.unizar.tfg_backend.core.usecases.LogFormularioHumanoUseCaseImpl
import java.time.LocalDate
import org.mockito.Mockito.`when`
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import kotlin.test.assertEquals

class LogFormularioHumanoUseCaseTest {
    private val repositorioFormularioHumano = mock<ServicioRepositorioFormularioHumano>()
    private val servicioETL = mock<ServicioETL>()
    private val repositorioFormularioMonitoreo = mock<ServicioRepositorioFormularioMonitoreo>()
    private val servicioEmail = mock<ServicioEmail>()

    private val casodeUso = LogFormularioHumanoUseCaseImpl(
        repositorioFormularioHumano,
        servicioETL,
        repositorioFormularioMonitoreo,
        servicioEmail,
        radioAlertaKm = 10.0
    )

    @Test
    fun `log guarda el formulario humano en el repositorio`() {
        val formulario = FormularioHumano(
            edad = 22,
            sexo = 'H',
            fechaCaso = LocalDate.now(),
            enfermedad = "Dengue",
            pais = "España",
            provinciaResidencia = 'Z',
            municipioResidencia = "Zaragoza",
            defuncion = false,
            hospitalizado = true,
            latitud = null,
            longitud = null
        )
        `when`(repositorioFormularioHumano.save(formulario)).thenReturn(formulario)
        val resultado = casodeUso.log(formulario)
        verify(repositorioFormularioHumano, times(1)).save(formulario)
        assertEquals(formulario, resultado)
    }

    @Test
    fun `log ejecuta el ETL siempre tras guardar el formulario`(){
        val formulario = FormularioHumano(
            edad = 22,
            sexo = 'H',
            fechaCaso = LocalDate.now(),
            enfermedad = "Dengue",
            pais = "España",
            provinciaResidencia = 'Z',
            municipioResidencia = "Zaragoza",
            defuncion = false,
            hospitalizado = true,
            latitud = null,
            longitud = null
        )
        `when`(repositorioFormularioHumano.save(formulario)).thenReturn(formulario)
        casodeUso.log(formulario)
        verify(servicioETL, times(1)).ejecutarETL()
    }

    @Test
    fun `log envia mail cuando hay vectores cercanos`(){
        val formulario = FormularioHumano(
            edad = 22,
            sexo = 'H',
            fechaCaso = LocalDate.now(),
            enfermedad = "Dengue",
            pais = "España",
            provinciaResidencia = 'Z',
            municipioResidencia = "",
            defuncion = false,
            hospitalizado = true,
            latitud = 41.59,
            longitud = -0.93
        )
        val vectorFalso = mock<FormularioMonitoreo>()
        val listaVectoresCercanos = listOf(vectorFalso)
        `when`(repositorioFormularioHumano.save(formulario)).thenReturn(formulario)
        `when`(repositorioFormularioMonitoreo.buscarVectoresEnRadio(
            formulario.latitud!!,
            formulario.longitud!!,
            10.0
        )).thenReturn(listaVectoresCercanos)
        casodeUso.log(formulario)
        verify(servicioEmail, times(1))
            .sendAlertaCasoHumanoCercaVectores(formulario.enfermedad, formulario.municipioResidencia, listaVectoresCercanos)
    }

}