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
import org.unizar.tfg_backend.infraestructure.repositories.RepositorioFormularioGarrapatas
import org.unizar.tfg_backend.infraestructure.repositories.RepositorioFormularioHumano
import org.unizar.tfg_backend.infraestructure.repositories.RepositorioFormularioMonitoreo
import org.unizar.tfg_backend.infraestructure.repositories.ServicioETL
import org.unizar.tfg_backend.infraestructure.repositories.ServicioEmailImpl
import org.unizar.tfg_backend.infraestructure.repositories.ServicioRepositorioFormularioGarrapatasImpl
import org.unizar.tfg_backend.infraestructure.repositories.ServicioRepositorioFormularioHumanoImpl
import org.unizar.tfg_backend.infraestructure.repositories.ServicioRepositorioFormularioMonitoreoImpl
import org.springframework.mail.javamail.JavaMailSender
import org.unizar.tfg_backend.core.ServicioEmail

@Configuration
class ApplicationConfiguration (
    private val repositorioFormularioHumano: RepositorioFormularioHumano,
    private val repositorioFormularioMonitoreo: RepositorioFormularioMonitoreo,
    private val repositorioFormularioGarrapatas: RepositorioFormularioGarrapatas,
    private val mailSender: JavaMailSender
) {
    @Bean
    fun servicioETL() = ServicioETL()

    @Bean
    open fun servicioEmail(): ServicioEmail = ServicioEmailImpl(mailSender)

    @Bean
    fun servicioRepositorioFormilarioHumano() =
        ServicioRepositorioFormularioHumanoImpl(repositorioFormularioHumano, servicioETL())

    @Bean
    fun servicioRepositorioFormularioMonitoreo() =
        ServicioRepositorioFormularioMonitoreoImpl(repositorioFormularioMonitoreo, servicioETL(), servicioEmail())

    @Bean
    fun servicioRepositorioFormularioGarrapatas() =
        ServicioRepositorioFormularioGarrapatasImpl(repositorioFormularioGarrapatas, servicioETL())

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
    fun obtenerFormulariosGarrapatasUseCase()
    = ObtenerFormulariosGarrapatasUseCaseImpl(servicioRepositorioFormularioGarrapatas())
}