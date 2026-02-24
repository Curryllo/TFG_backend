package org.unizar.tfg_backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration


@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class TfgBackendApplication

fun main(args: Array<String>) {
    runApplication<TfgBackendApplication>(*args)
}
