@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure

import jakarta.persistence.*
import java.time.LocalDate


@Entity
@Table(name = "form_hum")
@Suppress("LongParameterList")
class EntidadFormularioHumano(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "form_humano_gen")
    @SequenceGenerator(name = "form_humano_gen", sequenceName = "form_humano_seq", allocationSize = 1)
    val idForm: Int?,
    val edad: Short,
    val sexo: Char,
    val municipioCaso: Short?,
    val municipioResidencia: Short?,
    val municipioDeclarante: Short?,
    val fechaInicioSintomas: LocalDate,
    val defuncion: Boolean,
    val hospitalizado: Boolean
)