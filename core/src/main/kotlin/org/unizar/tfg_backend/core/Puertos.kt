@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.core

interface ServicioRepositorioFormularioHumano {

    fun save(form: FormularioHumano): FormularioHumano

    fun findAll(): List<FormularioHumano>
}

interface ServicioRepositorioFormularioMonitoreo {

    fun save(form: FormularioMonitoreo): FormularioMonitoreo

    fun findAll(): List<FormularioMonitoreo>

    fun buscarVectoresEnRadio(latitud: Double, longitud: Double, radioKm: Double): List<FormularioMonitoreo>
}

interface ServicioRepositorioFormularioGarrapatas {
    fun save(form: FormularioGarrapatas): FormularioGarrapatas

    fun findAll(): List<FormularioGarrapatas>
}

interface ServicioEmail {
    fun sendAlertaVectorInfectado(enfermedad: String, lugar: String?, vector: String)
    fun sendAlertaCasoHumanoCercaVectores(enfermedad: String, municipio: String, vectoresCercanos: List<FormularioMonitoreo>
    )
}

interface ServicioETL {
    fun ejecutarETL()
}

interface GeneradorToken {
    fun generarToken(email: String, rol: String, tiempo: Long): String
}

interface InformacionUsuario {
    fun autenticar(email: String, password: String): Usuario
}

data class TokensDominio(val tokenAcceso: String, val tokenRefresco: String)

interface ServicioAutenticacion {
    fun autenticar(email: String, password: String): TokensDominio

    fun registrar(usuario: Usuario)

    fun refrescarTokens(refreshToken: String): TokensDominio

    fun cerrarSesion(refreshToken: String)
}

interface ServicioRepositorioUsuarios {
    fun listadoSolicitudesRegistro(): List<Usuario>
    fun rechazarSolicitudRegistro(email: String)
    fun aprobarSolictudRegistro(email: String)
    fun eliminarUsuario(email: String)
    fun listadoUsuariosActivos(): List<Usuario>
}
