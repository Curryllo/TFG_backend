package org.unizar.tfg_backend.infraestrcuture.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.unizar.tfg_backend.core.ServicioAutenticacion
import org.unizar.tfg_backend.core.TokensDominio
import org.unizar.tfg_backend.core.Usuario
import org.unizar.tfg_backend.infraestructure.repositories.EntidadUsuario
import org.unizar.tfg_backend.infraestructure.repositories.RepositorioRefrescoToken
import org.unizar.tfg_backend.infraestructure.repositories.RepositorioUsuariosJpa


@Service
class ServicioAutenticacionImpl (
    private val authManager: AuthenticationManager,
    private val encoder: PasswordEncoder,
    private val userDetailsService: UserDetailsService,
    private val generadorToken: GeneradorTokenImpl,
    private val repositorioRefrescoToken: RepositorioRefrescoToken,
    private val repositorioUsuarios: RepositorioUsuariosJpa,
    @Value("\${jwt.accessTokenExpiration:86400000}") private val accessTokenExpiration: Long, // Por defecto 1 día
    @Value("\${jwt.refreshTokenExpiration:604800000}") private val refreshTokenExpiration: Long // Por defecto 7 días
) : ServicioAutenticacion {
    override fun autenticar(email: String, contrasena: String): TokensDominio {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                email,
                contrasena
            )
        )

        val user = userDetailsService.loadUserByUsername(email)

        val accessToken = createAccessToken(user)
        val refreshToken = createRefreshToken(user)


        return TokensDominio(
            tokenAcceso = accessToken,
            tokenRefresco = refreshToken
        )
    }

    override fun registrar(usuario: Usuario) {
        val contrasenaHash = encoder.encode(usuario.password)

        val entidad = EntidadUsuario(
            idUsuario = null,
            nombre = usuario.nombre,
            apellido1 = usuario.apellido1,
            apellido2 = usuario.apellido2,
            puesto = usuario.puesto,
            email = usuario.email,
            rol = usuario.rol,
            password = contrasenaHash
        )

        repositorioUsuarios.save(entidad)
    }

    fun refreshAccessToken(refreshToken: String): String {
        val username = generadorToken.extractEmail(refreshToken)

        return username.let { user ->
            val currentUserDetails = userDetailsService.loadUserByUsername(user)
            val refreshTokenUserDetails = repositorioRefrescoToken.findUserDetailsByToken(refreshToken)

            if (currentUserDetails.username == refreshTokenUserDetails?.username)
                createAccessToken(currentUserDetails)
            else
                throw AuthenticationServiceException("Invalid refresh token")
        }
    }

    private fun createAccessToken(user: UserDetails): String {
        val rol = user.authorities.firstOrNull()?.authority ?: "USER"
        return generadorToken.generarToken(user.username, rol)
    }

    private fun createRefreshToken(user: UserDetails): String {
        return generadorToken.generarRefreshToken(user.username, refreshTokenExpiration)
    }
}