package org.unizar.tfg_backend.infraestructure.repositories

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Repository

@Repository
open class RepositorioRefrescoToken(
    private val jpaRepository: RepositorioRefreshTokenJpa,
    private val userDetailsService: UserDetailsService
) {

    open fun findUserDetailsByToken(token: String): UserDetails? {
        // 1. Buscamos el token en PostgreSQL
        val entidad = jpaRepository.findByToken(token) ?: return null

        // 2. Si existe, cargamos los datos del usuario usando su email
        return userDetailsService.loadUserByUsername(entidad.emailUsuario)
    }

    open fun save(token: String, userDetails: UserDetails) {
        // Creamos la entidad mapeada
        val entidad = EntidadRefreshToken(
            id = null,
            token = token,
            emailUsuario = userDetails.username // En tu sistema, username es el email
        )

        // La guardamos en PostgreSQL
        jpaRepository.save(entidad)
    }

    open fun deleteByToken(token: String) {
        jpaRepository.deleteByToken(token)
    }
}