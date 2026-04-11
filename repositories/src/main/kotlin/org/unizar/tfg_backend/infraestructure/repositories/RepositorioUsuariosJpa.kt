package org.unizar.tfg_backend.infraestructure.repositories

import org.springframework.data.jpa.repository.JpaRepository

interface RepositorioUsuariosJpa : JpaRepository<EntidadUsuario,Long> {
    fun findByEmail(email: String): EntidadUsuario?
}