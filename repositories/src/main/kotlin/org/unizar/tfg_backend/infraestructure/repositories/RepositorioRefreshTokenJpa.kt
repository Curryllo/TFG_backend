package org.unizar.tfg_backend.infraestructure.repositories

import org.springframework.data.jpa.repository.JpaRepository

interface RepositorioRefreshTokenJpa : JpaRepository<EntidadRefreshToken, Long> {
    // Spring buscará en la base de datos por la columna 'token'
    fun findByToken(token: String): EntidadRefreshToken?

    // (Opcional) Muy útil si en el futuro quieres hacer un botón de "Cerrar sesión en todos los dispositivos"
    fun deleteByEmailUsuario(emailUsuario: String)
}