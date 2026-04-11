package org.unizar.tfg_backend.infraestructure.repositories

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.unizar.tfg_backend.core.ServicioEmail

@Service
open class ServicioEmailImpl(
    private val mailSender: JavaMailSender
) : ServicioEmail {
    @Async
    override fun sendAlertaVectorInfectado(enfermedad: String, lugar: String?, vector: String) {
        val mensaje = SimpleMailMessage()

        mensaje.from = "onboarding@resend.dev"
        mensaje.setTo("842545@unizar.es")
        mensaje.subject = "ALERTA: Vector infectado con $enfermedad"
        mensaje.text =
            """ALERTA de Salud Pública
                |Se ha registrado un vector portador de $enfermedad:
                |- Especie: $vector
                |- Zona: $lugar
            """.trimMargin()
        mailSender.send(mensaje)
    }
}