package org.unizar.tfg_backend.infraestructure.delivery

import org.unizar.tfg_backend.core.FormularioGarrapatas
import org.unizar.tfg_backend.core.FormularioHumano
import org.unizar.tfg_backend.core.FormularioMonitoreo

fun FormularioHumanosIn.toDomain() = FormularioHumano(
    edad = edad,
    sexo = sexo,
    fechaCaso = fechaCaso,
    enfermedad = enfermedad,
    pais = pais,
    provinciaResidencia = provinciaResidencia,
    municipioResidencia = municipioResidencia,
    defuncion = defuncion,
    hospitalizado = casoHospitalizado
)

fun FormularioMonitoreoIn.toDomain() = FormularioMonitoreo(
    lugarRecogida = lugarRecogida,
    vector = vector,
    enfermedad = enfermedad,
    fecha = fecha,
    numero = numero,
    genero = genero,
    longitud = longitud,
    latitud = latitud,
)

fun FormularioGarrapatasIn.toDomain() = FormularioGarrapatas(
    municipio = municipio,
    especie = especie,
    fecha = fecha,
    enHumano = enHumano,
    animal = animal
)