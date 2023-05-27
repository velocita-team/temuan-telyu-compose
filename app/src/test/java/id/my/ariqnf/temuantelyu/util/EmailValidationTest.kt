package id.my.ariqnf.temuantelyu.util

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class EmailValidationTest(private val email: String, private val expectedValue: Boolean) {

    @Test
    fun test() {
        val result = email.isValidEmail()
        assertEquals(expectedValue, result)
    }

    companion object {

        @JvmStatic
        @Parameterized.Parameters(name = "{index} - Email: {0} validity is {1}")
        fun dataTest(): List<Array<Any>> {
            return listOf(
                arrayOf("testing@mail.com", true),
                arrayOf("testing@mail.id", true),
                arrayOf("testing@mail.c", true),
                arrayOf("testing@mail.", false),
                arrayOf("testing@mail", false),
                arrayOf("testing@.com", false),
                arrayOf("testing@.", false),
                arrayOf("testing@", false),
                arrayOf("testing", false),
            )
        }
    }
}