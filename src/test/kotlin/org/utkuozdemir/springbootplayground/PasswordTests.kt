package org.utkuozdemir.springbootplayground

import org.junit.jupiter.api.Test
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder

class PasswordTests {
    @Test
    fun testBcrypt() {
        val encoded = BCryptPasswordEncoder().encode("faik")
        println(encoded)
    }

    @Test
    fun testScrypt() {
        val encoded = SCryptPasswordEncoder().encode("faik")
        println(encoded)
    }

    @Test
    fun testArgon2() {
        val encoded = Argon2PasswordEncoder().encode("faik")
        println(encoded)
    }

    @Test
    fun testPbkdf2() {
        val encoded = Pbkdf2PasswordEncoder().encode("faik")
        println(encoded)
    }
}
