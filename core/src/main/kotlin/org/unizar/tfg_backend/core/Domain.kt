@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core

import java.time.LocalDate


data class FormularioHumano(
    val edad: Short,
    val sexo: Char,
    val fechaCaso: LocalDate,
    val enfermedad: String,
    val pais: String,
    val provinciaResidencia: Char,
    val municipioResidencia: String,
    val defuncion: Boolean,
    val hospitalizado: Boolean
)

data class FormularioMonitoreo(
    val lugarRecogida: String?,
    val vector: String,
    val enfermedad: String?,
    val fecha: LocalDate,
    val numero: Int,
    val genero: Char?,
    val latitud: Double?,
    val longitud: Double?
)

data class FormularioGarrapatas(
    val municipio: String,
    val especie: String,
    val fecha: LocalDate,
    val enHumano: Boolean,
    val animal: String
)

data class Usuario(
    val nombre: String,
    val apellido1: String,
    val apellido2: String,
    val puesto: String,
    val email: String,
    val rol: String,
    val password: String
)