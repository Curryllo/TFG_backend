package org.unizar.tfg_backend.infraestructure.repositories

import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.http.Method
import org.springframework.stereotype.Service
import org.unizar.tfg_backend.core.ServicioMinIO
import java.util.concurrent.TimeUnit

@Service
class ServicioMinioImpl : ServicioMinIO{

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
}