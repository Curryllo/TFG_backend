@file:Suppress("SpellCheckingInspection")
package org.unizar.tfg_backend.infraestructure.repositories

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.unizar.tfg_backend.core.ServicioETL
import java.io.File

@Service
open class ServicioETLImpl : ServicioETL {
    @Async
    override fun ejecutarETL(){
        try{
            val nombreScript = "ProcesoETL.bat"
            val rutaScript = "/mnt/c/Users/Curro/Desktop/ScriptsTFG"
            println("Iniciando ejecución de Pentaho en segundo plano...")
            val process = ProcessBuilder("cmd.exe", "/c", nombreScript)
                .directory(File(rutaScript))
                .start()
            val exitCode = process.waitFor()
            println("ETL de Pentaho finalizado con código: $exitCode")
        } catch (e: Exception) {
            println("Error al intentar ejecutar Pentaho: ${e.message}")
        }
    }
}
