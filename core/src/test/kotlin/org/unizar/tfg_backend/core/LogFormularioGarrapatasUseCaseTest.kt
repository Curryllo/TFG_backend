package org.unizar.tfg_backend.core

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.unizar.tfg_backend.core.usecases.LogFormularioGarrapatasUseCaseImpl
import java.time.LocalDate
import kotlin.test.assertEquals

class LogFormularioGarrapatasUseCaseTest {
    private val repositorioFormularioGarrapatas = mock<ServicioRepositorioFormularioGarrapatas>()
    private val servicioETL = mock<ServicioETL>()

    private val casoDeUso = LogFormularioGarrapatasUseCaseImpl(
        repositorioFormularioGarrapatas,
        servicioETL
    )

    @Test
    fun `log guarda el formulario de monitoreo en el repositorio`(){
        val formulario = FormularioGarrapatas(
            municipio = "Borja",
            especie = "Ixodes ricinus",
            fecha = LocalDate.now(),
            enHumano = false,
            animal = "Corzo",
            longitud = null,
            latitud = null
        )
        `when`(repositorioFormularioGarrapatas.save(formulario)).thenReturn(formulario)
        val resultado = casoDeUso.log(formulario)
        verify(repositorioFormularioGarrapatas, times(1)).save(formulario)
        assertEquals(formulario, resultado)
    }

    @Test
    fun `log ejecuta el ETL siempre tras guardar el formulario`(){
        val formulario = FormularioGarrapatas(
            municipio = "Borja",
            especie = "Ixodes ricinus",
            fecha = LocalDate.now(),
            enHumano = false,
            animal = "Corzo",
            longitud = null,
            latitud = null
        )
        `when`(repositorioFormularioGarrapatas.save(formulario)).thenReturn(formulario)
        casoDeUso.log(formulario)
        verify(servicioETL, times(1)).ejecutarETL()
    }
}