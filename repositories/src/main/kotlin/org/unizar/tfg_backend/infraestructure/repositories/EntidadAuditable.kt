package org.unizar.tfg_backend.infraestructure.repositories

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import org.hibernate.envers.Audited

@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener::class)
abstract class EntidadAuditable {

    @CreatedDate
    @Column(name = "fecha_creacion", updatable = false)
    var fechaCreacion: LocalDateTime? = null

    @LastModifiedDate
    @Column(name = "fecha_modificacion")
    var fechaModificacion: LocalDateTime? = null

    @CreatedBy
    @Column(name = "creado_por", updatable = false)
    var creadoPor: String? = null

    @LastModifiedBy
    @Column(name = "modificado_por")
    var modificadoPor: String? = null
}