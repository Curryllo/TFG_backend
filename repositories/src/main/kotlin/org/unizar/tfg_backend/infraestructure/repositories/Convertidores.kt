@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure.repositories

import org.unizar.tfg_backend.core.FormularioHumano
import org.unizar.tfg_backend.core.FormularioMonitoreo

fun FormularioHumano.toEntity() = EntidadFormularioHumano(
    idForm = null,
    edad = edad,
    sexo = sexo,
    municipioCaso = municipioCaso,
    municipioResidencia = municipioResidencia,
    municipioDeclarante = municipioDeclarante,
    fechaInicioSintomas = fechaInicioSintomas,
    fechaActual = fechaActual,
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
    fechaActual = fechaActual,
    defuncion = defuncion,
    hospitalizado = hospitalizado
)

fun FormularioMonitoreo.toEntity() = EntidadFormularioMonitoreo(
    idForm = null,
    lugar = lugarRecogida,
    vector = vector,
    enfermedad = enfermedad,
    fecha = fecha,
    numero = numero,
    genero = genero,
    longitud = longitud,
    latitud = latitud,
)


fun EntidadFormularioMonitoreo.toDomain() = FormularioMonitoreo(
    lugarRecogida = lugar,
    vector = vector,
    enfermedad = enfermedad,
    fecha = fecha,
    numero = numero,
    genero = genero,
    latitud = latitud,
    longitud = longitud
)