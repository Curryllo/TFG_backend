@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure

import org.unizar.tfg_backend.core.FormularioHumano

fun FormularioHumano.toEntity() = EntidadFormularioHumano(
    idForm = null,
    edad = edad,
    sexo = sexo,
    municipioCaso = municipioCaso,
    municipioResidencia = municipioResidencia,
    municipioDeclarante = municipioDeclarante,
    fechaInicioSintomas = fechaInicioSintomas,
    defuncion = defuncion,
    hospitalizado = hospitalizado
)

fun EntidadFormularioHumano.toDomain() = FormularioHumano(
    edad = edad,
    sexo = sexo,
    municipioCaso = municipioCaso,
    municipioResidencia = municipioResidencia,
    municipioDeclarante = municipioDeclarante,
    fechaInicioSintomas = fechaInicioSintomas,
    defuncion = defuncion,
    hospitalizado = hospitalizado
)