package pitam.intelligentservice.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import pitam.intelligentservice.IntelligentService


object NotificationUtils {

    fun <T> IntelligentService<T>.buildCustomNotification(
        channelId: String,
        channelName: String,
        channelImportance: Int?,
        notificationIcon: Int,
        notificationTitle: String,
        notificationText: String,
        customize: NotificationCompat.Builder.() -> NotificationCompat.Builder = { this }
    ): Notification {
        val notificationManager =
            getSystemService(LifecycleService.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                channelImportance ?: NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(notificationIcon)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .customize()
            .build()

        notificationManager.notify(notificationId, notification)

        return notification
    }


}