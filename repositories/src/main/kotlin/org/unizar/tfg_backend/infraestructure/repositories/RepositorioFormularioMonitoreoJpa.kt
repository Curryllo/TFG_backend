package org.unizar.tfg_backend.infraestructure.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface RepositorioFormularioMonitoreoJpa : JpaRepository<EntidadFormularioMonitoreo,Long> {
    @Query(
        value = """
        SELECT * FROM form_mon
        WHERE latitud IS NOT NULL AND longitud IS NOT NULL
        AND (6371 * acos(
            cos(radians(:lat)) * cos(radians(latitud)) *
            cos(radians(longitud) - radians(:lon)) +
            sin(radians(:lat)) * sin(radians(latitud))
        )) <= :radioKm
    """,
        nativeQuery = true
    )
    fun findVectoresEnRadio(
        @Param("lat") latitud: Double,
        @Param("lon") longitud: Double,
        @Param("radioKm") radioKm: Double
    ): List<EntidadFormularioMonitoreo>

}