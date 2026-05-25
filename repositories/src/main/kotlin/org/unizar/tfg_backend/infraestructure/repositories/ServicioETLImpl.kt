@file:Suppress("SpellCheckingInspection")
package org.unizar.tfg_backend.infraestructure.repositories

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.unizar.tfg_backend.core.ServicioETL
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.lambda.LambdaClient
import software.amazon.awssdk.services.lambda.model.InvocationType
import software.amazon.awssdk.services.lambda.model.InvokeRequest

@Service
open class ServicioETLImpl : ServicioETL {
    private val lambdaClient: LambdaClient = LambdaClient.builder()
        .region(Region.EU_NORTH_1) // Tu región de Estocolmo
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build()

    @Async
    override fun ejecutarETL(){
        /*
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
         */
        try {
            println("Iniciando ejecución de ETL en AWS Lambda en segundo plano...")

            val request = InvokeRequest.builder()
                .functionName("tfg-curro-proceso-etl") // Asegúrate de que este es el nombre exacto de tu Lambda
                .invocationType(InvocationType.EVENT) // Le dice a AWS: "Dispara la Lambda y no esperes respuesta"
                .build()

            lambdaClient.invoke(request)

            println("✅ Señal de ETL enviada a AWS con éxito")
        } catch (e: Exception) {
            println("❌ Error al intentar invocar la Lambda del ETL: ${e.message}")
        }
    }
}
