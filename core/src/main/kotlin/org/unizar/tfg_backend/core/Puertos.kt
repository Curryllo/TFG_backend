@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core

interface ServicioRepositorioFormularioHumano {

    fun save(form: FormularioHumano): FormularioHumano
}