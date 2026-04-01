@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core

interface ServicioRepositorioFormularioHumano {

    fun save(form: FormularioHumano): FormularioHumano

    fun findAll(): List<FormularioHumano>
}

interface ServicioRepositorioFormularioMonitoreo {

    fun save(form: FormularioMonitoreo): FormularioMonitoreo

    fun findAll(): List<FormularioMonitoreo>
}

interface ServicioRepositorioFormularioGarrapatas {
    fun save(form: FormularioGarrapatas): FormularioGarrapatas

    fun findAll(): List<FormularioGarrapatas>
}

interface  ServicioEmail {
    fun sendAlertaVectorInfectado(enfermedad: String, lugar: String?, vector: String)
}