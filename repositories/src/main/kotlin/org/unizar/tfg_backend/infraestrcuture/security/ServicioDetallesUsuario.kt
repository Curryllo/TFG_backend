package org.unizar.tfg_backend.infraestrcuture.security // Tu paquete de seguridad

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.unizar.tfg_backend.infraestructure.repositories.RepositorioUsuariosJpa

@Service // 🟢 Esta es la etiqueta clave que Spring estaba buscando
class ServicioDetallesUsuario(
    // 🟢 1. INYECTA AQUÍ TU REPOSITORIO DE USUARIOS REAL (Descomenta esto y pon el tuyo)
    private val repositorioUsuario: RepositorioUsuariosJpa
) : UserDetailsService {

    // Este método lo llama Spring Security automáticamente cada vez que alguien hace login o manda un token
    override fun loadUserByUsername(email: String): UserDetails {

        // 🟢 2. BUSCAMOS AL USUARIO EN TU BASE DE DATOS
        val usuario = repositorioUsuario.findByEmail(email)
            ?: throw UsernameNotFoundException("No se encontró el usuario con email: $email")

        /* ⚠️ HACK PARA PROBAR AHORA MISMO (Borra este bloque cuando descomentes lo de arriba)
        Si aún no tienes la tabla de usuarios creada, puedes usar este usuario "quemado"
        para probar que toda la arquitectura funciona.

        if (email != "curro@unizar.es") {
            throw UsernameNotFoundException("Usuario no encontrado")
        }
        val passwordEncriptada = "\$2a\$10\$X/yD.6O1i6Vwz.s7e.mD1O8JvB6z3L9/5v7z9q9w2Z2X8y9w2Z2X" // Equivale a "1234"
        val rol = "USER"
        // -------------------------------------------------------------------------
        */
        // 🟢 3. CONVERTIMOS TU USUARIO AL FORMATO DE SPRING SECURITY
        return User.builder()
            .username(usuario.email) // usuario.email
            .password(usuario.password) // usuario.password (¡Debe estar encriptada con BCrypt en tu BD!)
            .roles(usuario.rol) // usuario.rol.name
            .build()
    }
}