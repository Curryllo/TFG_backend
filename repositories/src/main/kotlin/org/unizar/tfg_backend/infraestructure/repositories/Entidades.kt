@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure.repositories

import jakarta.persistence.*
import java.time.LocalDate


@Entity
@Table(name = "form_hum")
@Suppress("LongParameterList")
class EntidadFormularioHumano(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "form_humano_gen")
    @SequenceGenerator(name = "form_humano_gen", sequenceName = "form_humano_seq", allocationSize = 1)
    @Column(name = "idform")
    val idForm: Int?,
    @Column(name = "edad")
    val edad: Short,
    @Column(name = "sexo")
    val sexo: Char,
    @Column(name = "municipiocaso")
    val municipioCaso: Short?,
    @Column(name = "municipioresidencia")
    val municipioResidencia: Short?,
    @Column(name = "municipiodeclarante")
    val municipioDeclarante: Short?,
    @Column(name = "fechainiciosintomas")
    val fechaInicioSintomas: LocalDate?,
    @Column(name = "fechac  aso")
    val fechaActual: LocalDate,
    @Column(name = "defuncion")
    val defuncion: Boolean,
    @Column(name = "casohospitalizado")
    val hospitalizado: Boolean
)


@Entity
@Table(name = "form_mon")
@Suppress("LongParameterList")
class EntidadFormularioMonitoreo(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "form_mon_gen")
    @SequenceGenerator(name = "form_mon_gen", sequenceName = "form_mon_seq", allocationSize = 1)
    @Column(name = "idform")
    val idForm: Int?,
    @Column(name = "lugar")
    val lugar: String?,
    @Column(name = "vector")
    val vector: String,
    @Column(name = "enfermedad")
    val enfermedad: String,
    @Column(name = "fecha")
    val fecha: LocalDate,
    @Column(name = "numero")
    val numero: Int,
    @Column(name = "genero")
    val genero: Char?,
    @Column(name = "longitud")
    val longitud: Double?,
    @Column(name = "latitud")
    val latitud: Double?
)

