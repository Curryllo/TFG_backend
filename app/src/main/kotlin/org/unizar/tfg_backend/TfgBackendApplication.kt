package org.unizar.tfg_backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@EnableWebSecurity
@SpringBootApplication
@EnableAsync
@EnableScheduling
class TfgBackendApplication

fun main(args: Array<String>) {
    runApplication<TfgBackendApplication>(*args)
}
