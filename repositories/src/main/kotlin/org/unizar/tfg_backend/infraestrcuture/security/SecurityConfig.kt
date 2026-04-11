package org.unizar.tfg_backend.infraestrcuture.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
open class SecurityConfig(
    // 🟢 1. Inyectamos tu filtro personalizado
    private val filtroAutenticacion: FiltroAutenticacion,
    // 🟢 2. Inyectamos el servicio que busca usuarios en TU base de datos
    private val userDetailsService: UserDetailsService
) {

    @Bean
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() } // Apagamos CSRF (obligatorio al usar JWT)
            .authorizeHttpRequests { auth ->
                auth
                    // 🔓 Dejamos las puertas abiertas SOLO para hacer login y pedir refresco
                    .requestMatchers("/api/auth/login", "/api/auth/refresh", "/api/auth/singIn").permitAll()
                    // 🔒 Cualquier otra ruta requiere un JWT válido
                    .anyRequest().authenticated()
            }
            .sessionManagement { session ->
                // No guardamos sesiones en la memoria del servidor
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider())
            // 🟢 3. Metemos tu filtro justo antes de que Spring intente leer contraseñas
            .addFilterBefore(filtroAutenticacion, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    open fun authenticationProvider(): AuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        // Le decimos a Spring cómo buscar a tus usuarios...
        provider.setUserDetailsService(userDetailsService)
        // ...y cómo desencriptar sus contraseñas
        provider.setPasswordEncoder(passwordEncoder())
        return provider
    }

    @Bean
    open fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        // Esta es la forma moderna de crear el Manager en Spring Boot 3
        return config.authenticationManager
    }

    @Bean
    open fun passwordEncoder(): PasswordEncoder {
        // Encriptación industrial estándar
        return BCryptPasswordEncoder()
    }
}