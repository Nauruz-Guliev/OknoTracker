package utils.validation

import java.util.regex.Pattern

interface Validator {
    fun validate(field: String): Boolean
}

private class SimpleValidator(patternString: String) : Validator {
    private val pattern = Pattern.compile(patternString)

    override fun validate(field: String): Boolean = pattern.matcher(field).matches()
}

fun Validator(pattern: String): Validator = SimpleValidator(pattern)