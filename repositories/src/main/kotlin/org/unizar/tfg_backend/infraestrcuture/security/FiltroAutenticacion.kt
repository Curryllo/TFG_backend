package org.unizar.tfg_backend.infraestrcuture.security

import io.jsonwebtoken.ExpiredJwtException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.FilterChain
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource

@Component
class FiltroAutenticacion (
    private val servicioDetallesUsuario: ServicioDetallesUsuario,
    private val generadorToken: GeneradorTokenImpl
) : OncePerRequestFilter() {
    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader: String? = request.getHeader("Authorization")

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                // Sacamos el texto después de "Bearer "
                val token: String = authorizationHeader.substringAfter("Bearer ")

                // 🟢 Usamos tu método extractEmail
                val username: String = generadorToken.extractEmail(token)

                if (SecurityContextHolder.getContext().authentication == null) {
                    val userDetails: UserDetails = servicioDetallesUsuario.loadUserByUsername(username)

                    if (username == userDetails.username) {
                        val authToken = UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.authorities
                        )
                        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = authToken
                    }
                }
            } catch (ex: ExpiredJwtException) {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.contentType = "application/json"
                response.writer.write("""{"error": "Token caducado", "code": "TOKEN_EXPIRED"}""")
                return
            } catch (ex: Exception) {
                // 🟢 Mejorado: Devolvemos un 401 real y evitamos que explote
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.contentType = "application/json"
                response.writer.write("""{"error": "Token invalido o caducado"}""")

                // 🛑 EL RETURN SALVAVIDAS: Evita que siga la cadena de filtros
                return
            }
        }

        // Si no hay token, simplemente sigue su camino (Spring Security decidirá si lo bloquea o no)
        filterChain.doFilter(request, response)
    }
}