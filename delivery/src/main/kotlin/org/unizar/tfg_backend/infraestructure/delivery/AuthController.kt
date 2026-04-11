package org.unizar.tfg_backend.infraestructure.delivery

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.unizar.tfg_backend.core.usecases.InicarSesionUseCase
import org.unizar.tfg_backend.core.usecases.RegistrarUseCase

@RestController
@RequestMapping("/api/auth")
class AuthController(
    val iniciarSesionUseCase: InicarSesionUseCase,
    val registrarUseCase: RegistrarUseCase
) {
    @PostMapping("/login")
    fun login(@RequestBody request: LoginIn): LoginOut {
        val tokens = iniciarSesionUseCase.iniciarSesion(request.mail, request.password)
        return LoginOut(
            tokenAcceso = tokens.tokenAcceso,
            tokenRefresco = tokens.tokenRefresco
        )
    }
    @PostMapping("/singIn")
    fun singIn(@RequestBody request: SingIn) {
        val datos = request.toDomain()
        registrarUseCase.registrar(datos)
    }
}