package org.unizar.tfg_backend.core.usecases

import org.unizar.tfg_backend.core.ServicioMinIO

interface DescargarArchivoMinIOUseCase {
    fun descargar(cubo: String, objeto: String) : String
}

class DescargarArchivoMinIOUseCaseImpl(
    private val servicioMinIO: ServicioMinIO
) : DescargarArchivoMinIOUseCase {
    override fun descargar(cubo: String, objeto: String) : String{
        return servicioMinIO.generarUrlDescarga(cubo, objeto)
    }
}