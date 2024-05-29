package ru.kpfu.itis.features.notifications

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import org.koin.java.KoinJavaComponent.getKoin
import java.util.concurrent.TimeUnit

actual fun commonNotificationScheduler(): CommonNotificationsScheduler {
    return getKoin().get<CommonNotificationsScheduler>()
}

class NotificationScheduler(
    private val workManager: WorkManager
) : CommonNotificationsScheduler {

    override fun schedule(notificationId: Int, repeatInterval: Long, triggerTime: Long) {
        val workRequest: PeriodicWorkRequest = PeriodicWorkRequest
            .Builder(NotificationWorker::class.java, repeatInterval, TimeUnit.MILLISECONDS)
            .setInitialDelay(triggerTime, TimeUnit.MILLISECONDS)
            .build()
        workManager.enqueueUniquePeriodicWork(
            notificationId.toString(),
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    override fun removeScheduling(notificationId: Int) {
        workManager.cancelUniqueWork(notificationId.toString())
    }
}