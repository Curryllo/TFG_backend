@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.unizar.tfg_backend.core.usecases.LogFormularioHumanoUseCaseImpl
import org.unizar.tfg_backend.core.usecases.LogFormularioMonitoreoUseCaseImpl
import org.unizar.tfg_backend.core.usecases.ObtenerFormulariosHumanosUseCaseImpl
import org.unizar.tfg_backend.infraestructure.repositories.RepositorioFormularioHumano
import org.unizar.tfg_backend.infraestructure.repositories.RepositorioFormularioMonitoreo
import org.unizar.tfg_backend.infraestructure.repositories.ServicioETL
import org.unizar.tfg_backend.infraestructure.repositories.ServicioRepositorioFormularioHumanoImpl
import org.unizar.tfg_backend.infraestructure.repositories.ServicioRepositorioFormularioMonitoreoImpl

@Configuration
class ApplicationConfiguration (
    private val repositorioFormularioHumano: RepositorioFormularioHumano,
    private val repositorioFormularioMonitoreo: RepositorioFormularioMonitoreo
) {
    @Bean
    fun servicioETL() = ServicioETL()
    @Bean
    fun servicioRepositorioFormilarioHumano() = ServicioRepositorioFormularioHumanoImpl(repositorioFormularioHumano)

    @Bean
    fun servicioRepositorioFormularioMonitoreo() =
        ServicioRepositorioFormularioMonitoreoImpl(repositorioFormularioMonitoreo, servicioETL())

    @Bean
    fun logFormularioHumanoUseCase() = LogFormularioHumanoUseCaseImpl(servicioRepositorioFormilarioHumano())

    @Bean
    fun obtenerFormulariosHumanosUseCase() = ObtenerFormulariosHumanosUseCaseImpl(servicioRepositorioFormilarioHumano())

    @Bean
    fun logFormularioMonitoreoUseCase() = LogFormularioMonitoreoUseCaseImpl(servicioRepositorioFormularioMonitoreo())
}