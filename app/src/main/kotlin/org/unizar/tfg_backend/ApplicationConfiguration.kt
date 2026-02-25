@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.unizar.tfg_backend.core.usecases.LogFormularioHumanoUseCaseImpl
import org.unizar.tfg_backend.core.usecases.ObtenerFormulariosHumanosUseCaseImpl
import org.unizar.tfg_backend.infraestructure.repositories.RepositorioFormularioHumano
import org.unizar.tfg_backend.infraestructure.repositories.ServicioRepositorioFormularioHumanoImpl

@Configuration
class ApplicationConfiguration (
    private val repositorioFormularioHumano: RepositorioFormularioHumano
) {
    @Bean
    fun servicioRepositorioFormilarioHumano() = ServicioRepositorioFormularioHumanoImpl(repositorioFormularioHumano)

    @Bean
    fun logFormularioHumanoUseCase() = LogFormularioHumanoUseCaseImpl(servicioRepositorioFormilarioHumano())

    @Bean
    fun obtenerFormulariosHumanosUseCase() = ObtenerFormulariosHumanosUseCaseImpl(servicioRepositorioFormilarioHumano())
}