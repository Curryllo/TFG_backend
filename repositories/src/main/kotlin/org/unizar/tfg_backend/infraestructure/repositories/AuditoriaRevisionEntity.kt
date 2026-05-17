package org.unizar.tfg_backend.infraestructure.repositories

import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.envers.DefaultRevisionEntity
import org.hibernate.envers.RevisionEntity

@Entity
@Table(name = "revinfo_auditoria")
@RevisionEntity(AuditoriaRevisionListener::class)
class AuditoriaRevisionEntity : DefaultRevisionEntity() {

    var usuario: String = "Desconocido"
}