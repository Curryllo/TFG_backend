package org.unizar.tfg_backend.infraestrcuture.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Service
import org.unizar.tfg_backend.core.GeneradorToken
import java.util.Base64
import java.util.Date
import javax.crypto.spec.SecretKeySpec

@Service
class GeneradorTokenImpl (
    private val secret: String = "aW5jcmVpYmxlbWVudGVTZWd1cmFDbGF2ZVBhcmFFbFRGR0RlQ3Vycm8yMDI2QVBJ"
): GeneradorToken {

    private val signingKey: SecretKeySpec
        get() {
            val keyBytes: ByteArray = Base64.getDecoder().decode(secret)
            return SecretKeySpec(keyBytes, 0, keyBytes.size, "HmacSHA256")
        }

    override fun generarToken(email: String, rol: String) : String {

        val expirationDate = Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)

        return Jwts.builder()
            .setSubject(email)
            .claim("rol", rol)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(expirationDate)
            .signWith(signingKey)
            .compact()
    }

    fun extractEmail(token: String): String {
        return extractAllClaims(token).subject
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun generarRefreshToken(email: String, tokenRefresh: Long) : String {
        val fechaActual = System.currentTimeMillis()
        val fechaExpiracion = java.util.Date(fechaActual + tokenRefresh)

        // 2. Construimos el JWT
        return io.jsonwebtoken.Jwts.builder()
            .setSubject(email) // Identificamos de quién es este token
            .setIssuedAt(java.util.Date(fechaActual)) // Fecha de creación
            .setExpiration(fechaExpiracion) // Cuándo caduca (los 7 días)
            .signWith(signingKey) // Lo firmamos con tu clave secreta
            .compact()
    }
}