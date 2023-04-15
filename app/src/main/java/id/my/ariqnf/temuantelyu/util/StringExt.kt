package id.my.ariqnf.temuantelyu.util

import android.util.Patterns
import java.util.regex.Pattern

private const val MIN_PASS_LENGTH = 6
private const val PASS_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$"

private const val MAX_PASS_LENGTH = 70
private const val FULL_NAME_PATTERN = "^[\\p{L} ]{2,}$"

fun String.isValidEmail(): Boolean {
    return this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return this.isNotBlank() &&
            this.length >= MIN_PASS_LENGTH &&
            Pattern.compile(PASS_PATTERN).matcher(this).matches()
}

fun String.passwordMatches(repeated: String): Boolean {
    return this == repeated
}

fun String.isValidName(): Boolean {
    return this.isNotBlank() &&
            this.length <= MAX_PASS_LENGTH &&
            Pattern.compile(FULL_NAME_PATTERN).matcher(this).matches()
}