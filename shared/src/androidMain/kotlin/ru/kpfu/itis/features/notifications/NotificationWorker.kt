package ru.kpfu.itis.features.notifications

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.koin.java.KoinJavaComponent.inject

class NotificationWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    private val notification: ONotification by inject(ONotification::class.java)

    override fun doWork(): Result {
        notification.show()
        return Result.success()
    }
}