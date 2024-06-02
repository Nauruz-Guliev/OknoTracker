package ru.kpfu.itis

import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource
import ru.kpfu.itis.shared.MR

// TODO() to generate resources use `./gradlew generateMRcommonMain`
object OResources {

    object Common {
        fun errorIcon() = MR.images.wifi_sign
        fun datePickerDialogTitle() = MR.strings.date_picker_dialog_title
        fun timePickerDialogTitle() = MR.strings.time_picker_dialog_title
        fun pickerDialogConfirmButton() = MR.strings.picker_dialog_confirm_button
        fun pickerDialogCancelButton() = MR.strings.picker_dialog_cancel_button
        fun deleteText() = MR.strings.delete
    }

    object SignUp {
        fun icon() = MR.images.person_with_laptop
    }

    object Login {
        fun title() = MR.strings.login_title

        fun emailLabel() = MR.strings.login_email_label
        fun emailError() = MR.strings.login_email_error
        fun password() = MR.strings.login_password_label
        fun passwordError() = MR.strings.login_password_error

        fun icon() = MR.images.woman_sitting_cross_legged_with_a_laptop
    }

    object Statistics {
        fun title() = MR.strings.statistics_title
        fun description() = MR.strings.statistics_description

        fun completedTasks() = MR.strings.statistics_completed_tasks
        fun completedOnTimeTasks() = MR.strings.statistics_completed_on_time_tasks

        fun remainingTasks() = MR.strings.statistics_remaining_tasks

        fun statisticsImage(): ImageResource = MR.images.statistics_man_with_prize
        fun statisticsImageContentDescription() = MR.strings.statistics_man_with_prize_image

        fun emptyStatisticsImage(): ImageResource = MR.images.statistics_team
        fun emptyStatisticsLabel(): StringResource = MR.strings.statistics_empty
        fun emptyStatisticsDescription() = MR.strings.statistics_empty_description
    }

    object Settings {
        fun title() = MR.strings.settings_title
        fun logout() = MR.strings.settings_item_logout
        fun successfulLogout() = MR.strings.setting_successful_logout
    }

    object Notification {
        fun notificationChannelName() = MR.strings.notifications_channel_name
        fun notificationChannelDescription() = MR.strings.notifications_channel_description
        fun notificationChannelId() = MR.strings.notifications_channel_id
        fun notificationIcon() = MR.images.reminder_notification_icon
        fun notificationContent() = MR.strings.notifications_content
        fun notificationAreNotEnabled() = MR.strings.notifications_are_not_enabled
    }

    object SingleTask {
        fun singleTaskNameError() = MR.strings.task_name_error
        fun singleTaskNameLabel() = MR.strings.task_name_label
        fun singleTaskDescriptionError() = MR.strings.task_description_error
        fun singleTaskDescriptionLabel() = MR.strings.task_description_label
    }
}