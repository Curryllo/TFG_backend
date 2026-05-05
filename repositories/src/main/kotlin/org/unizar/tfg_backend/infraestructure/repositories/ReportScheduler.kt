package org.unizar.tfg_backend.infraestructure.repositories

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.unizar.tfg_backend.core.ServicioEmail


@Component
class ReportScheduler (
    private val servicioPdf: ServicioPdf,
    private val servicioEmail: ServicioEmail
){

    @Scheduled(cron = "0 0 0 1 * *")
    fun generarYEnviarInforme() {
        val pdf = servicioPdf.generarInforme()
        servicioEmail.sendInforme(pdf)
        println(">>> Email enviado")
    }
}