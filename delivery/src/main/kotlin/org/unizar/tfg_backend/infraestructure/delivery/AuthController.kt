package org.unizar.tfg_backend.infraestructure.delivery

import org.springframework.web.bind.annotation.*
import org.unizar.tfg_backend.core.usecases.CerrarSesionUseCase
import org.unizar.tfg_backend.core.usecases.InicarSesionUseCase
import org.unizar.tfg_backend.core.usecases.RefrescarTokenUseCase
import org.unizar.tfg_backend.core.usecases.RegistrarUseCase

@RestController
@RequestMapping("/api/auth")
class AuthController(
        val iniciarSesionUseCase: InicarSesionUseCase,
        val registrarUseCase: RegistrarUseCase,
        val refrescarTokenUseCase: RefrescarTokenUseCase,
        val cerrarSesionUseCase: CerrarSesionUseCase
) {
    @PostMapping("/login")
    fun login(@RequestBody request: LoginIn): LoginOut {
        val tokens = iniciarSesionUseCase.iniciarSesion(request.mail, request.password)
        return LoginOut(tokenAcceso = tokens.tokenAcceso, tokenRefresco = tokens.tokenRefresco)
    }

    @GetMapping("/refresh")
    fun refresh(@CookieValue("refreshToken") refreshToken: String): LoginOut {
        val tokens = refrescarTokenUseCase.refrescarTokens(refreshToken)
        return LoginOut(tokenAcceso = tokens.tokenAcceso, tokenRefresco = tokens.tokenRefresco)
    }

    @PostMapping("/logout")
    fun logout(@CookieValue("refreshToken") refreshToken: String) {
        cerrarSesionUseCase.cerrarSesion(refreshToken)
    }

    @PostMapping("/singIn")
    fun singIn(@RequestBody request: SingIn) {
        println("Datos de la solicitud de registro $request")
        val datos = request.toDomain()
        registrarUseCase.registrar(datos)
    }
}
