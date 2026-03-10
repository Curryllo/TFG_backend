@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core

import java.time.LocalDate


data class FormularioHumano(
    val edad: Short,
    val sexo: Char,
    val municipioCaso: Short?,
    val municipioResidencia: Short?,
    val municipioDeclarante: Short?,
    val fechaInicioSintomas: LocalDate?,
    val fechaActual: LocalDate,
    val defuncion: Boolean,
    val hospitalizado: Boolean
)

data class FormularioMonitoreo(
    val lugarRecogida: String?,
    val vector: String,
    val enfermedad: String,
    val fecha: LocalDate,
    val numero: Int,
    val genero: Char?,
    val latitud: Double?,
    val longitud: Double?
)