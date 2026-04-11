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

interface ServicioRepositorioUsuarios {
    fun save(usuario: Usuario): Usuario

    fun findAll(): List<Usuario>

    fun findByEmail(email: String): Usuario?
}


interface ServicioEmail {
    fun sendAlertaVectorInfectado(enfermedad: String, lugar: String?, vector: String)
}

interface GeneradorToken {
    fun generarToken(email: String, rol: String) : String
}

interface InformacionUsuario {
    fun autenticar(email: String, password: String) : Usuario
}

data class TokensDominio(
    val tokenAcceso: String,
    val tokenRefresco: String
)

interface ServicioAutenticacion {
    fun autenticar(email: String, password: String) : TokensDominio

    fun registrar(usuario: Usuario)
}
