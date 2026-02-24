@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core

import java.time.LocalDate


data class FormularioHumano(
    val edad: Short,
    val sexo: Char,
    val municipioCaso: Short?,
    val municipioResidencia: Short?,
    val municipioDeclarante: Short?,
    val fechaInicioSintomas: LocalDate,
    val defuncion: Boolean,
    val hospitalizado: Boolean
)