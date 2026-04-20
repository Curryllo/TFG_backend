package org.unizar.tfg_backend.infraestructure.delivery

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.unizar.tfg_backend.core.usecases.AprobarSolicitudRegistroUseCase
import org.unizar.tfg_backend.core.usecases.EliminarUsuarioUseCase
import org.unizar.tfg_backend.core.usecases.ObtenerSolicitudesRegistroUseCase
import org.unizar.tfg_backend.core.usecases.ObtenerUsuariosActivosUseCase
import org.unizar.tfg_backend.core.usecases.RechazarSolicitudesRegistroUseCase


@RestController
@RequestMapping("/api/admin")
class AdminController(
    private val obtenerSolicitudesRegistroUseCase: ObtenerSolicitudesRegistroUseCase,
    private val rechazarSolicitudesRegistroUseCase: RechazarSolicitudesRegistroUseCase,
    private val aprobarSolicitudRegistroUseCase: AprobarSolicitudRegistroUseCase,
    private val eliminarUsuarioUseCase: EliminarUsuarioUseCase,
    private val obtenerUsuariosActivosUseCase: ObtenerUsuariosActivosUseCase
) {

    @GetMapping("/solicitudes")
    fun obtenerSolicitudesPendientes(): ResponseEntity<Any> {
        val lista = obtenerSolicitudesRegistroUseCase.obtenerSolicitudesRegistro()
        println("Lista de solicitudes $lista")
        return if (lista.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(lista)
        }
    }

    @PostMapping("/solicitudes/{email}/aprobar")
    fun aprobarSolicitudPendiente(@PathVariable email: String) {
        aprobarSolicitudRegistroUseCase.aprobarSolicitudRegistro(email)
    }

    @DeleteMapping("/solicitudes/{email}/rechazar")
    fun eliminarSolicitudPendiente(@PathVariable email: String) {
        rechazarSolicitudesRegistroUseCase.rechazarSolicitudesRegistro(email)
    }

    @DeleteMapping("/eliminar/{email}")
    fun eliminarUsuario(@PathVariable email: String){
        eliminarUsuarioUseCase.eliminarUsuario(email)
    }

    @GetMapping("/usuarios")
    fun obtenerUsuariosActivos(): ResponseEntity<Any> {
        val lista = obtenerUsuariosActivosUseCase.obtenerUsuariosActivos()
        return if (lista.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(lista)
        }
    }


}