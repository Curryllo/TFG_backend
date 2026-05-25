@file:Suppress("SpellCheckingInspection")
package org.unizar.tfg_backend.infraestructure.repositories

import org.springframework.stereotype.Service
import org.unizar.tfg_backend.core.ServicioMinIO
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import java.time.Duration

@Service
class ServicioS3Impl : ServicioMinIO{

    private val s3Client : S3Client = S3Client.builder()
        .region(Region.EU_NORTH_1)
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build()

    private val s3Presigner: S3Presigner = S3Presigner.builder()
        .region(Region.EU_NORTH_1)
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build()

    /*
    private val minioClient: MinioClient = MinioClient.builder()
        .endpoint("http://localhost:9000")
        .credentials("E9EAN4872C2HEFSEMKPH", "SSTUSnqKRMq6jPKoHFntb5cjGKhknn9S4LN+73bc")
        .build()

    override fun generarUrlDescarga(cubo: String, objeto: String) : String {
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(cubo)
                .`object`(objeto)
                .expiry(1, TimeUnit.MINUTES) // El enlace caduca en 5 minutos
                .build()
        )
    }

    fun leerCSV(cubo: String, objeto: String) : List<Map<String, String>> {
        val stream = minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(cubo)
                .`object`(objeto)
                .build()
        )

        val lineas = stream.bufferedReader().readLines()
        if (lineas.isEmpty()) return emptyList()

        val cabeceras = lineas[0].split(";")

        return lineas.drop(1)
            .filter { it.isNotBlank() }
            .map { linea ->
                val valores = linea.split(";")
                cabeceras.zip(valores).toMap()
            }

     */

    override fun generarUrlDescarga(cubo: String, objeto: String): String {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(cubo)
            .key(objeto)
            .build()

        val presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(1)) // El enlace caduca en 1 minuto
            .getObjectRequest(getObjectRequest)
            .build()

        val presignedUrl = s3Presigner.presignGetObject(presignRequest)
        return presignedUrl.url().toString()
    }

    fun leerCSV(cubo: String, objeto: String): List<Map<String, String>> {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(cubo)
            .key(objeto)
            .build()

        val stream = s3Client.getObject(getObjectRequest)

        val lineas = stream.bufferedReader().readLines()
        if (lineas.isEmpty()) return emptyList()

        val separador = ";"
        val cabeceras = lineas[0].split(separador)

        return lineas.drop(1)
            .filter { it.isNotBlank() }
            .map { linea ->
                // Mapeamos los valores con la cabecera
                val valores = linea.split(separador)

                // Evitamos un fallo si alguna fila tiene menos columnas por un error de formato
                if (valores.size == cabeceras.size) {
                    cabeceras.zip(valores).toMap()
                } else {
                    emptyMap()
                }
            }.filter { it.isNotEmpty() }
    }

}