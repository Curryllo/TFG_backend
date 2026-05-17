package org.unizar.tfg_backend.infraestructure.repositories

import org.hibernate.envers.RevisionListener
import org.springframework.security.core.context.SecurityContextHolder

class AuditoriaRevisionListener : RevisionListener {

    override fun newRevision(revisionEntity: Any) {
        val revision = revisionEntity as AuditoriaRevisionEntity

        val autenticacion = SecurityContextHolder.getContext().authentication

        if (autenticacion != null && autenticacion.isAuthenticated && autenticacion.principal != "anonymousUser") {
            revision.usuario = autenticacion.name
        } else {
            revision.usuario = "Sistema_Importacion"
        }
    }
}