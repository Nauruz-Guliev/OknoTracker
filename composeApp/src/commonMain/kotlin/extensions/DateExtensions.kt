package extensions

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

fun LocalDateTime.convertToString(remainTime: Boolean = false): String {
    val clockNow = Clock.System.now()
    val currentTime = clockNow.toLocalDateTime(TimeZone.UTC)
    return if (date.year == currentTime.year) {
        val yesterday =
            clockNow.minus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault()).toLocalDateTime(
                TimeZone.UTC
            )
        val tomorrow =
            clockNow.plus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault()).toLocalDateTime(
                TimeZone.UTC
            )

        return when (this.dayOfYear) {
            yesterday.dayOfYear -> "Yesterday"
            currentTime.dayOfYear -> "Today"
            tomorrow.dayOfYear -> "Tomorrow"
            else -> getDateAsString(this, remainTime)
        }
    } else {
        getDateAsString(this, remainTime)
    }
}

private fun getDateAsString(dateTime: LocalDateTime, remainTime: Boolean): String {
    return if (!remainTime) {
        dateTime.date.toString()
    } else {
        dateTime.toString()
    }
}

fun LocalDateTime.isPastTime(): Boolean {
    val currentTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)
    return if (currentTime.year > this.year) {
        false
    } else {
        currentTime.dayOfYear > this.dayOfYear
    }
}