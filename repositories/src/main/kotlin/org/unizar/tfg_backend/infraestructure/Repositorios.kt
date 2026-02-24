@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure

import org.springframework.data.jpa.repository.JpaRepository

interface RepositorioFormularioHumano : JpaRepository<EntidadFormularioHumano,Long>