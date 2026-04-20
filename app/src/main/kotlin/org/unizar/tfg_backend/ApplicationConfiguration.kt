@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.unizar.tfg_backend.core.ServicioAutenticacion
import org.unizar.tfg_backend.core.ServicioETL
import org.unizar.tfg_backend.core.ServicioEmail
import org.unizar.tfg_backend.core.usecases.AprobarSolicitudRegistroUseCase
import org.unizar.tfg_backend.core.usecases.AprobarSolicitudRegistroUseCaseImpl
import org.unizar.tfg_backend.core.usecases.CerrarSesionUseCaseImpl
import org.unizar.tfg_backend.core.usecases.EliminarUsuarioUseCaseImpl
import org.unizar.tfg_backend.core.usecases.InicarSesionUseCaseImpl
import org.unizar.tfg_backend.core.usecases.LogFormularioGarrapatasUseCaseImpl
import org.unizar.tfg_backend.core.usecases.LogFormularioHumanoUseCaseImpl
import org.unizar.tfg_backend.core.usecases.LogFormularioMonitoreoUseCaseImpl
import org.unizar.tfg_backend.core.usecases.ObtenerFormulariosGarrapatasUseCaseImpl
import org.unizar.tfg_backend.core.usecases.ObtenerFormulariosHumanosUseCaseImpl
import org.unizar.tfg_backend.core.usecases.ObtenerFormulariosMonitoreoUseCaseImpl
import org.unizar.tfg_backend.core.usecases.ObtenerSolicitudesRegistroUseCaseImpl
import org.unizar.tfg_backend.core.usecases.ObtenerUsuariosActivosImpl
import org.unizar.tfg_backend.core.usecases.RechazarSolicitudesRegistroUseCaseImpl
import org.unizar.tfg_backend.core.usecases.RefrescarTokenUseCaseImpl
import org.unizar.tfg_backend.core.usecases.RegistrarUseCaseImpl
import org.unizar.tfg_backend.infraestructure.repositories.RepositorioFormularioGarrapatasJpa
import org.unizar.tfg_backend.infraestructure.repositories.RepositorioFormularioHumanoJpa
import org.unizar.tfg_backend.infraestructure.repositories.RepositorioFormularioMonitoreoJpa
import org.unizar.tfg_backend.infraestructure.repositories.RepositorioUsuariosJpa
import org.unizar.tfg_backend.infraestructure.repositories.ServicioETLImpl
import org.unizar.tfg_backend.infraestructure.repositories.ServicioRepositorioFormularioGarrapatasImpl
import org.unizar.tfg_backend.infraestructure.repositories.ServicioRepositorioFormularioHumanoImpl
import org.unizar.tfg_backend.infraestructure.repositories.ServicioRepositorioFormularioMonitoreoImpl
import org.unizar.tfg_backend.infraestructure.repositories.ServicioRepositorioUsuariosImpl

@Configuration
class ApplicationConfiguration(
        private val repositorioFormularioHumanoJpa: RepositorioFormularioHumanoJpa,
        private val repositorioFormularioMonitoreoJpa: RepositorioFormularioMonitoreoJpa,
        private val repositorioFormularioGarrapatasJpa: RepositorioFormularioGarrapatasJpa,
        private val repositorioUsuariosJpa: RepositorioUsuariosJpa,
        private val servicioAutenticacion: ServicioAutenticacion,
        private val servicioEmail: ServicioEmail,
        private val servicioETL: ServicioETL,
) {

    @Bean
    fun servicioRepositorioFormilarioHumano() =
            ServicioRepositorioFormularioHumanoImpl(repositorioFormularioHumanoJpa)

    @Bean
    fun servicioRepositorioFormularioMonitoreo() =
            ServicioRepositorioFormularioMonitoreoImpl(repositorioFormularioMonitoreoJpa)

    @Bean
    fun servicioRepositorioFormularioGarrapatas() =
            ServicioRepositorioFormularioGarrapatasImpl(repositorioFormularioGarrapatasJpa)

    @Bean
    fun servicioRepositorioUsuarios() =
            ServicioRepositorioUsuariosImpl(repositorioUsuariosJpa)

    @Bean
    fun logFormularioHumanoUseCase() =
            LogFormularioHumanoUseCaseImpl(
                servicioRepositorioFormilarioHumano(), servicioETL,
                servicioRepositorioFormularioMonitoreo(), servicioEmail)

    @Bean
    fun obtenerFormulariosHumanosUseCase() =
            ObtenerFormulariosHumanosUseCaseImpl(servicioRepositorioFormilarioHumano())

    @Bean
    fun logFormularioMonitoreoUseCase() =
            LogFormularioMonitoreoUseCaseImpl(
                servicioRepositorioFormularioMonitoreo(),
                servicioEmail,
                servicioETL)

    @Bean
    fun logFormularioGarrapatasUseCase() =
            LogFormularioGarrapatasUseCaseImpl(servicioRepositorioFormularioGarrapatas(), servicioETL)

    @Bean
    fun obtenerFormulariosMonitoreoUseCase() =
            ObtenerFormulariosMonitoreoUseCaseImpl(servicioRepositorioFormularioMonitoreo())

    @Bean
    fun obtenerFormulariosGarrapatasUseCase() =
            ObtenerFormulariosGarrapatasUseCaseImpl(servicioRepositorioFormularioGarrapatas())

    @Bean fun iniciarSesionUseCase() = InicarSesionUseCaseImpl(servicioAutenticacion)

    @Bean fun registrarUseCase() = RegistrarUseCaseImpl(servicioAutenticacion)

    @Bean
    fun refrescarTokenUseCase() =
            RefrescarTokenUseCaseImpl(servicioAutenticacion)

    @Bean
    fun cerrarSesionUseCase() = CerrarSesionUseCaseImpl(servicioAutenticacion)

    @Bean
    fun obtenerSolicitudesRegistroUseCase() = ObtenerSolicitudesRegistroUseCaseImpl(servicioRepositorioUsuarios())

    @Bean
    fun rechazarSolicitudRegistroUseCase() = RechazarSolicitudesRegistroUseCaseImpl(servicioRepositorioUsuarios())

    @Bean
    fun aprobarSolicitudRegistroUseCase() = AprobarSolicitudRegistroUseCaseImpl(servicioRepositorioUsuarios())

    @Bean
    fun eliminarUsuarioUseCase() = EliminarUsuarioUseCaseImpl(servicioRepositorioUsuarios())

    @Bean
    fun obtenerUsuariosActivosUseCase() = ObtenerUsuariosActivosImpl(servicioRepositorioUsuarios())
}
