package org.unizar.tfg_backend.infraestructure.repositories

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EntityScan(basePackages = ["org.unizar.tfg_backend.infraestructure.repositories"])
@EnableJpaRepositories(basePackages = ["org.unizar.tfg_backend.infraestructure.repositories"])
open class TestConfig{

}