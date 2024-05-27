package features.signin

import pro.respawn.flowmvi.api.MVIState
import ru.kpfu.itis.common.model.ErrorModel

sealed interface SignInState : MVIState {
    fun findFieldError(field: InputField): String? =
        (this as? ValidationError)?.fieldErrors?.find { it == field }?.errorText

    data object Initial : SignInState
    data object Loading : SignInState
    data class ValidationError(val fieldErrors: List<InputField>) : SignInState
    data class InternalError(val error: ErrorModel) : SignInState
    data class NetworkError(val email: String, val password: String): SignInState
}

enum class InputField(
    val regex: String,
    val errorText: String,
    val labelText: String,
    val maxLength: Int?
) {
    PASSWORD(
        regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,20}$",
        errorText = "The password must consist of 8 to 20 characters, the use of Cyrillic alphabet is prohibited",
        labelText = "Password",
        maxLength = 20,
    ),
    EMAIL(
        regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
        errorText = "The email must contain characters, followed by an '@' symbol, a domain name with alphanumeric characters and periods, and ends with a top-level domain that is 2 to 6 letters long, the use of Cyrillic alphabet is prohibited",
        labelText = "Email",
        maxLength = null
    )
}