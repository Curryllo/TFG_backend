@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure.repositories

import org.springframework.data.jpa.repository.JpaRepository

interface RepositorioFormularioHumano : JpaRepository<EntidadFormularioHumano,Long>