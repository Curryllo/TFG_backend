@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure.repositories

import org.unizar.tfg_backend.core.FormularioGarrapatas
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioGarrapatas
import org.unizar.tfg_backend.core.FormularioHumano
import org.unizar.tfg_backend.core.FormularioMonitoreo
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioHumano
import org.unizar.tfg_backend.core.ServicioRepositorioFormularioMonitoreo
import org.unizar.tfg_backend.core.ServicioRepositorioUsuarios
import org.unizar.tfg_backend.core.Usuario

class ServicioRepositorioFormularioMonitoreoImpl(
    private val repositorioFormularioMonitoreoJpa: RepositorioFormularioMonitoreoJpa
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
        val resultado = repositorioFormularioMonitoreoJpa.save(form.toEntity()).toDomain()
        return resultado
    }


    override fun findAll(): List<FormularioMonitoreo> {
        val list = repositorioFormularioMonitoreoJpa.findAll()

        return list.map { it.toDomain() }
    }

    override fun buscarVectoresEnRadio(latitud: Double, longitud: Double, radioKm: Double): List<FormularioMonitoreo> {
        val list = repositorioFormularioMonitoreoJpa.findVectoresEnRadio(latitud, longitud, radioKm)

        return list.map { it.toDomain() }
    }
}

class ServicioRepositorioFormularioHumanoImpl(
    private val repositorioFormularioHumanoJpa: RepositorioFormularioHumanoJpa
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
        val resultado = repositorioFormularioHumanoJpa.save(form.toEntity()).toDomain()
        return resultado
    }


    override fun findAll(): List<FormularioHumano> {
        val list = repositorioFormularioHumanoJpa.findAll()

        return list.map { it.toDomain() }
    }
}

class ServicioRepositorioFormularioGarrapatasImpl(
    private val repositorioFormularioGarrapatasJpa: RepositorioFormularioGarrapatasJpa
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
        val resultado = repositorioFormularioGarrapatasJpa.save(form.toEntity()).toDomain()
        return resultado
    }

    override fun findAll(): List<FormularioGarrapatas> {
        val list = repositorioFormularioGarrapatasJpa.findAll()
        return list.map { it.toDomain() }
    }
}

class ServicioRepositorioUsuariosImpl(
    private val repositorioUsuariosJpa: RepositorioUsuariosJpa
) : ServicioRepositorioUsuarios {
    override fun listadoSolicitudesRegistro(): List<Usuario> {
        val list = repositorioUsuariosJpa.findByEstado("Pendiente")
        return list.map { it.toDomain() }
    }

    override fun rechazarSolicitudRegistro(email: String) {
        val solicitud = repositorioUsuariosJpa.findByEmail(email)
            ?: throw NoSuchElementException("No se encontró ninguna solicitud pendiente para el email: $email")
        repositorioUsuariosJpa.delete(solicitud)
    }

    override fun aprobarSolictudRegistro(email: String) {
        val solicitud = repositorioUsuariosJpa.findByEmail(email)
            ?: throw NoSuchElementException("No se encontró ninguna solicitud pendiente para el email: $email")

        solicitud.estado = "Activo"
        repositorioUsuariosJpa.save(solicitud)
    }

    override fun eliminarUsuario(email: String) {
        val usuario = repositorioUsuariosJpa.findByEmail(email)
            ?: throw NoSuchElementException("No se encontró ningún usuario con el email: $email")
        repositorioUsuariosJpa.delete(usuario)
    }

    override fun listadoUsuariosActivos(): List<Usuario> {
        val list = repositorioUsuariosJpa.findByEstado("Activo")
        return list.map { it.toDomain() }
    }
}




