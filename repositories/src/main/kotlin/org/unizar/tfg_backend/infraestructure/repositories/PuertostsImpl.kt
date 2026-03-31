@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure.repositories

import org.unizar.tfg_backend.core.FormularioGarrapatas
import org.unizar.tfg_backend.core.FormularioHumano
import org.unizar.tfg_backend.core.FormularioMonitoreo
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioGarrapatas
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioHumano
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioMonitoreo

class ServicioRepositorioFormularioHumanoImpl(
    private val repositorioFormularioHumano: RepositorioFormularioHumano,
    private val servicioETL: ServicioETL
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

    override fun save(form: FormularioHumano): FormularioHumano {
        val resultado = repositorioFormularioHumano.save(form.toEntity()).toDomain()
        servicioETL.ejecutarETL()
        return resultado
    }


    override fun findAll(): List<FormularioHumano> {
        val list = repositorioFormularioHumano.findAll()

        return list.map { it.toDomain() }
    }
}

class ServicioRepositorioFormularioMonitoreoImpl(
    private val repositorioFormularioMonitoreo: RepositorioFormularioMonitoreo,
    private val servicioETL: ServicioETL
) : ServicioRepositorioFormularioMonitoreo {

    /**
     * Guarda un formulario de monitoreo entomologico en la base de datos.
     *
     * Pasos:
     * 1. Convierte el objeto del dominio [FormularioMonitoreo] a JPA [EntidadFormularioMonitoreo]
     * 2. Guarda la entidad usando el repositorio JPA
     * 3. Convierte la entidad guardada de vuelta a un objeto de dominio
     * 4. Devuelve el objeto de dominio guardado
     *
     * **Transaction Management:**
     * - Database transaction is managed by Spring's @Transactional
     * - Automatic rollback on exceptions
     * - Optimistic locking for concurrent modifications
     *
     * @param form El [FormularioMonitoreo] objeto del domimio a guardar
     * @return El [FormularioMonitoreo] objeto de dominio (incluye el ID generado)
     */

    override fun save(form: FormularioMonitoreo): FormularioMonitoreo {
        val resultado = repositorioFormularioMonitoreo.save(form.toEntity()).toDomain()
        servicioETL.ejecutarETL()
        return resultado
    }


    override fun findAll(): List<FormularioMonitoreo> {
        val list = repositorioFormularioMonitoreo.findAll()

        return list.map { it.toDomain() }
    }
}

class ServicioRepositorioFormularioGarrapatasImpl(
    private val repositorioFormularioGarrapatas: RepositorioFormularioGarrapatas
) : ServicioRepositorioFormularioGarrapatas {

    /**
     * Guarda un formulario de monitoreo entomologico en la base de datos.
     *
     * Pasos:
     * 1. Convierte el objeto del dominio [FormularioGarrapatas] a JPA [EntidadFormularioGarrapata]
     * 2. Guarda la entidad usando el repositorio JPA
     * 3. Convierte la entidad guardada de vuelta a un objeto de dominio
     * 4. Devuelve el objeto de dominio guardado
     *
     * **Transaction Management:**
     * - Database transaction is managed by Spring's @Transactional
     * - Automatic rollback on exceptions
     * - Optimistic locking for concurrent modifications
     *
     * @param form El [FormularioGarrapatas] objeto del domimio a guardar
     * @return El [FormularioGarrapatas] objeto de dominio (incluye el ID generado)
     */

    override fun save(form: FormularioGarrapatas): FormularioGarrapatas {
        val resultado = repositorioFormularioGarrapatas.save(form.toEntity()).toDomain()
        return resultado
    }

    override fun findAll(): List<FormularioGarrapatas> {
        val list = repositorioFormularioGarrapatas.findAll()
        return list.map { it.toDomain() }
    }
}