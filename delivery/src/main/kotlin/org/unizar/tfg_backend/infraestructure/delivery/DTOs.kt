@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure.delivery

import java.time.LocalDate

data class FormularioHumanosIn (
    val edad: Short = 0,
    val sexo: Char = 'U',
    val fechaCaso: LocalDate = LocalDate.now(),
    val enfermedad: String = "",
    val pais: String = "",
    val provinciaResidencia: Char = 'U',
    val municipioResidencia: String = "",
    val defuncion: Boolean = false,
    val casoHospitalizado: Boolean = false
)

data class FormularioMonitoreoIn(
    val lugarRecogida: String? = null,
    val vector: String = "",
    val enfermedad: String? = null,
    val fecha: LocalDate = LocalDate.now(),
    val numero: Int = 1,
    val genero: Char? = 'M',
    val latitud: Double? = null,
    val longitud: Double? = null
)

data class FormularioGarrapatasIn(
    val municipio: String = "",
    val especie: String = "",
    val fecha: LocalDate = LocalDate.now(),
    val enHumano: Boolean = false,
    val animal: String = ""
)