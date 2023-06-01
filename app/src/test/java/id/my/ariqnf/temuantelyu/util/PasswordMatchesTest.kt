package id.my.ariqnf.temuantelyu.util

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class PasswordMatchesTest(private val password: String,
                          private val repeatedPassword: String,
                          private val expectedValue: Boolean) {

    @Test
    fun test() {
        val result = password.passwordMatches(repeatedPassword)
        assertEquals(expectedValue, result)
    }

    companion object {

        @JvmStatic
        @Parameterized.Parameters(name = "{index} - {1} is same with {0} ({2})")
        fun dataTest(): List<Array<Any>> {
            return listOf(
                arrayOf("Password123", "Password123", true),
                arrayOf("Password123", "password123", false),
            )
        }
    }
}