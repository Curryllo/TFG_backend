@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure.repositories

import org.unizar.tfg_backend.core.FormularioHumano
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioHumano

class ServicioRepositorioFormularioHumanoImpl(
    private val repositorioFormularioHumano: RepositorioFormularioHumano
) : ServicioRepositorioFormularioHumano {

    /**
     * Guarda un formulario de caso humano en la base de datos.
     *
     * Pasos:
     * 1. Convierte el objeto del dominio [FormularioHumano] a JPA [EntidadFormularioHumano]
     * 2. Guarda la entidad usando el repositorio JPA
     * 3. Convierte la entidad guardada de vuelta a un objeto de dominio
     * 4. Devuelve el objeto de dominio guardado
     *
     * **Transaction Management:**
     * - Database transaction is managed by Spring's @Transactional
     * - Automatic rollback on exceptions
     * - Optimistic locking for concurrent modifications
     *
     * @param form El [FormularioHumano] objeto del domimio a guardar
     * @return El [FormularioHumano] objeto de dominio (incluye el ID generado)
     */

    override fun save(form: FormularioHumano): FormularioHumano =
        repositorioFormularioHumano.save(form.toEntity()).toDomain()

    override fun findAll(): List<FormularioHumano> {
        val list = repositorioFormularioHumano.findAll()

        return list.map { it.toDomain() }
    }
}