@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.unizar.tfg_backend.core.usecases.LogFormularioGarrapatasUseCaseImpl
import org.unizar.tfg_backend.core.usecases.LogFormularioHumanoUseCaseImpl
import org.unizar.tfg_backend.core.usecases.LogFormularioMonitoreoUseCaseImpl
import org.unizar.tfg_backend.core.usecases.ObtenerFormulariosGarrapatasUseCaseImpl
import org.unizar.tfg_backend.core.usecases.ObtenerFormulariosHumanosUseCaseImpl
import org.unizar.tfg_backend.core.usecases.ObtenerFormulariosMonitoreoUseCaseImpl
import org.unizar.tfg_backend.infraestructure.repositories.RepositorioFormularioGarrapatasJpa
import org.unizar.tfg_backend.infraestructure.repositories.RepositorioFormularioHumanoJpa
import org.unizar.tfg_backend.infraestructure.repositories.RepositorioFormularioMonitoreoJpa
import org.unizar.tfg_backend.infraestructure.repositories.ServicioETL
import org.unizar.tfg_backend.infraestructure.repositories.ServicioRepositorioFormularioGarrapatasImpl
import org.unizar.tfg_backend.infraestructure.repositories.ServicioRepositorioFormularioHumanoImpl
import org.unizar.tfg_backend.infraestructure.repositories.ServicioRepositorioFormularioMonitoreoImpl
import org.unizar.tfg_backend.core.ServicioAutenticacion
import org.unizar.tfg_backend.core.ServicioEmail
import org.unizar.tfg_backend.core.usecases.InicarSesionUseCaseImpl
import org.unizar.tfg_backend.core.usecases.RegistrarUseCase
import org.unizar.tfg_backend.core.usecases.RegistrarUseCaseImpl

@Configuration
class ApplicationConfiguration (
    private val repositorioFormularioHumanoJpa: RepositorioFormularioHumanoJpa,
    private val repositorioFormularioMonitoreoJpa: RepositorioFormularioMonitoreoJpa,
    private val repositorioFormularioGarrapatasJpa: RepositorioFormularioGarrapatasJpa,
    private val servicioAutenticacion: ServicioAutenticacion,
    private val servicioEmail: ServicioEmail
) {
    @Bean
    fun servicioETL() = ServicioETL()

    @Bean
    fun servicioRepositorioFormilarioHumano() =
        ServicioRepositorioFormularioHumanoImpl(repositorioFormularioHumanoJpa, servicioETL())

    @Bean
    fun servicioRepositorioFormularioMonitoreo() =
        ServicioRepositorioFormularioMonitoreoImpl(repositorioFormularioMonitoreoJpa, servicioETL(), servicioEmail)

    @Bean
    fun servicioRepositorioFormularioGarrapatas() =
        ServicioRepositorioFormularioGarrapatasImpl(repositorioFormularioGarrapatasJpa, servicioETL())



    @Bean
    fun logFormularioHumanoUseCase() =
        LogFormularioHumanoUseCaseImpl(servicioRepositorioFormilarioHumano())

    @Bean
    fun obtenerFormulariosHumanosUseCase() =
        ObtenerFormulariosHumanosUseCaseImpl(servicioRepositorioFormilarioHumano())

    @Bean
    fun logFormularioMonitoreoUseCase() =
        LogFormularioMonitoreoUseCaseImpl(servicioRepositorioFormularioMonitoreo())

    @Bean
    fun logFormularioGarrapatasUseCase() =
        LogFormularioGarrapatasUseCaseImpl(servicioRepositorioFormularioGarrapatas())

    @Bean
    fun obtenerFormulariosMonitoreoUseCase() =
        ObtenerFormulariosMonitoreoUseCaseImpl(servicioRepositorioFormularioMonitoreo())

    @Bean
    fun obtenerFormulariosGarrapatasUseCase() =
        ObtenerFormulariosGarrapatasUseCaseImpl(servicioRepositorioFormularioGarrapatas())

    @Bean
    fun iniciarSesionUseCase() = InicarSesionUseCaseImpl(servicioAutenticacion)

    @Bean
    fun registrarUseCase() = RegistrarUseCaseImpl(servicioAutenticacion)

}