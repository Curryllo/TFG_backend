package org.unizar.tfg_backend.infraestrcuture.security // Tu paquete de seguridad

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.unizar.tfg_backend.infraestructure.repositories.RepositorioUsuariosJpa

@Service
class ServicioDetallesUsuario(
    private val repositorioUsuario: RepositorioUsuariosJpa
) : UserDetailsService {

    // Este método lo llama Spring Security automáticamente cada vez que alguien hace login o manda un token
    override fun loadUserByUsername(email: String): UserDetails {


        val usuario = repositorioUsuario.findByEmail(email)
            ?: throw UsernameNotFoundException("No se encontró el usuario con email: $email")

        val cuentaDeshabilitada = usuario.estado != "Activo"

        return User.builder()
            .username(usuario.email)
            .password(usuario.password)
            .roles(usuario.rol)
            .disabled(cuentaDeshabilitada)
            .build()
    }
}