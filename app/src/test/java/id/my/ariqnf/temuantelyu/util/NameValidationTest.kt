package id.my.ariqnf.temuantelyu.util

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class NameValidationTest(private val name: String, private val expectedValue: Boolean) {

    @Test
    fun test() {
        val result = name.isValidName()
        assertEquals(expectedValue, result)
    }

    companion object {

        @JvmStatic
        @Parameterized.Parameters(name = "{index} - Name: {0} validity is {1}")
        fun dataTest(): List<Array<Any>> {
            return listOf(
                arrayOf("Andi", true),
                arrayOf("Adi", true),
                arrayOf("Ay", true),
                arrayOf("A", false),
                arrayOf("@Andi", false),
                arrayOf("@Adi", false),
                arrayOf("@Ay", false),
                arrayOf("@A", false),
                arrayOf("Andi2", false),
                arrayOf("Adi2", false),
                arrayOf("Ay2", false),
                arrayOf("A2", false),
                arrayOf("@Andi2", false),
                arrayOf("@Adi2", false),
                arrayOf("@Ay2", false),
                arrayOf("@A2", false),
                arrayOf("@Andi2", false),
                arrayOf("@Adi2", false),
            )
        }
    }
}