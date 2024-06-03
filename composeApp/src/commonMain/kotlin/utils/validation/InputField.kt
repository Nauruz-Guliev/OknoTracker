package utils.validation

import dev.icerock.moko.resources.StringResource
import ru.kpfu.itis.OResources

enum class InputField(
    val regex: String,
    val errorText: StringResource,
    val labelText: StringResource,
    val maxLength: Int?
) {
    PASSWORD(
        regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,20}$",
        errorText = OResources.Login.passwordError(),
        labelText = OResources.Login.password(),
        maxLength = 20,
    ),
    EMAIL(
        regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
        errorText = OResources.Login.emailError(),
        labelText = OResources.Login.emailLabel(),
        maxLength = null
    ),
    NAME(
        regex = "^.{1,50}$",
        errorText = OResources.SingleTask.nameError(),
        labelText = OResources.SingleTask.nameLabel(),
        maxLength = 50
    ),
    DESCRIPTION(
        regex = "^.{1,500}$",
        errorText = OResources.SingleTask.descriptionError(),
        labelText = OResources.SingleTask.fieldDescriptionLabel(),
        maxLength = 500
    )
}