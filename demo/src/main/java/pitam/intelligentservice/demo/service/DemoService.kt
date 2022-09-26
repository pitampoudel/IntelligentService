package pitam.intelligentservice.demo.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import pitam.intelligentservice.IntelligentService
import pitam.intelligentservice.demo.MainActivity
import pitam.intelligentservice.demo.R
import pitam.intelligentservice.utils.NotificationUtils.buildCustomNotification

@AndroidEntryPoint
class DemoService : IntelligentService<String>(notificationId = 1) {

    inner class LocalBinder : Binder() {
        fun getService() = this@DemoService
    }

    private val binder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        updateNotificationIfShowing("It can take a while")
        //TODO whatever you want to do
        //TODO  setKeepRunningWithNotification(false) when work is completed
        return super.onStartCommand(intent, flags, startId)
    }


    override fun buildNotification(data: String?): Notification {
        return buildCustomNotification(
            channelId = "CHANNEL_ID_DEMO",
            channelName = "Demo Channel",
            channelImportance = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) NotificationManager.IMPORTANCE_LOW else null,
            notificationIcon = R.drawable.ic_launcher_foreground,
            notificationTitle = "Service is running",
            notificationText = data?:"Please wait"
        ) {

            // Tapping the notification opens MainActivity
            val startActivityIntent = PendingIntent.getActivity(
                this@DemoService,
                0,
                Intent(this@DemoService, MainActivity::class.java),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
            )

            setOngoing(true)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setContentIntent(startActivityIntent)
        }
    }


}


