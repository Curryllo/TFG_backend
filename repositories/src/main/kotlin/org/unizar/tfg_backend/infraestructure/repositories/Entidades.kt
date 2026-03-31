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
    @Column(name = "fechacaso")
    val fechaCaso: LocalDate,
    @Column(name = "enfermedad")
    val enfermedad: String,
    @Column(name = "pais")
    val pais: String,
    @Column(name = "provinciaresidencia")
    val provinciaResidencia: Char,
    @Column(name = "municipioresidencia")
    val municipioResidencia: String,
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
    val enfermedad: String?,
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

@Entity
@Table(name = "form_garr")
@Suppress("LongParameterList")
class EntidadFormularioGarrapata(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "form_garr_gen")
    @SequenceGenerator(name = "form_garr_gen", sequenceName = "form_garr_seq", allocationSize = 1)
    @Column(name = "idform")
    val idForm: Int?,
    @Column(name = "municipiorecogida")
    val municipioRecogida: String,
    @Column(name = "especie")
    val especie: String,
    @Column(name = "fecharecogida")
    val fechaRecogida: LocalDate,
    @Column(name = "enhumano")
    val enHumano: Boolean,
    @Column(name = "animal")
    val animal: String
)

