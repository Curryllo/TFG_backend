package org.unizar.tfg_backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.scheduling.annotation.EnableAsync


@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
@EnableAsync
class TfgBackendApplication

fun main(args: Array<String>) {
    runApplication<TfgBackendApplication>(*args)
}
