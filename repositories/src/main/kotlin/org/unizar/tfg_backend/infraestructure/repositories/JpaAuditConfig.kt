package org.unizar.tfg_backend.infraestructure.repositories

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder
import java.util.Optional

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
open class JpaAuditConfig {

    @Bean
    open fun auditorProvider(): AuditorAware<String> {
        return AuditorAware {

            val autenticacion = SecurityContextHolder.getContext().authentication

            if (autenticacion != null && autenticacion.isAuthenticated && autenticacion.principal != "anonymousUser") {
                Optional.of(autenticacion.name)
            } else {
                Optional.of("Sistema_Importacion")
            }
        }
    }
}