@file:Suppress("SpellCheckingInspection")

package org.unizar.tfg_backend.infraestructure.repositories

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
open class RepositorioRefreshTokenJpaTest {

    @Autowired
    lateinit var repositorio: RepositorioRefreshTokenJpa

    @BeforeEach
    fun setup() {
        repositorio.deleteAll()
    }

    @Test
    fun `save guarda un token y le asigna un id`() {
        val guardado = repositorio.save(entidadToken("token-123", "usuario@test.com"))
        assertNotNull(guardado.id)
        assertEquals("token-123", guardado.token)
    }

    @Test
    fun `findByToken devuelve el token cuando existe`() {
        repositorio.save(entidadToken("token-123", "usuario@test.com"))

        val resultado = repositorio.findByToken("token-123")

        assertNotNull(resultado)
        assertEquals("usuario@test.com", resultado?.emailUsuario)
    }

    @Test
    fun `findByToken devuelve null cuando el token no existe`() {
        val resultado = repositorio.findByToken("token-inexistente")
        assertNull(resultado)
    }

    @Test
    fun `deleteByToken elimina el token de la base de datos`() {
        repositorio.save(entidadToken("token-123", "usuario@test.com"))

        repositorio.deleteByToken("token-123")

        assertNull(repositorio.findByToken("token-123"))
    }

    @Test
    fun `deleteByToken no lanza excepcion cuando el token no existe`() {
        assertDoesNotThrow {
            repositorio.deleteByToken("token-inexistente")
        }
    }

    // --- Helper ---

    private fun entidadToken(token: String, email: String) = EntidadRefreshToken(
        id = null,
        token = token,
        emailUsuario = email
    )
}