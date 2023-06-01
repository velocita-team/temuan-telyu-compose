package id.my.ariqnf.temuantelyu.util

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class PasswordValidationTest(private val password: String, private val expectedValue: Boolean) {

    @Test
    fun test() {
        val result = password.isValidPassword()
        assertEquals(expectedValue, result)
    }

    companion object {

        @JvmStatic
        @Parameterized.Parameters(name = "{index} - Password: {0} validity is {1}")
        fun dataTest(): List<Array<Any>> {
            return listOf(
                arrayOf("Passw27", true),
                arrayOf("Pass27", true),
                arrayOf("Pas27", false),
                arrayOf("passw27", false),
                arrayOf("pass27", false),
                arrayOf("pas27", false),
                arrayOf("Passwor", false),
                arrayOf("Passo", false),
                arrayOf("Passw", false),
                arrayOf("passwor", false),
                arrayOf("passo", false),
                arrayOf("passw", false),
            )
        }
    }
}