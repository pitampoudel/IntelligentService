package pitam.intelligentservice

import android.app.Notification
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import timber.log.Timber

abstract class IntelligentService(val notificationId: Int) : LifecycleService() {
    private var bindCount = 0

    private fun handleBind() {
        bindCount++
        Timber.d("++ => bind count = $bindCount => starting service")
        startService(Intent(this, this::class.java))
    }

    override fun onBind(intent: Intent): IBinder? {
        handleBind()
        return super.onBind(intent)
    }

    override fun onRebind(intent: Intent?) {
        handleBind()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Timber.d("service started => managing lifetime")
        manageLifetime()
        // In case we are stopped by the system, have the system restart this service so we can
        // manage our lifetime appropriately.
        return START_STICKY
    }


    override fun onUnbind(intent: Intent?): Boolean {
        bindCount--
        Timber.d("-- => bind count = $bindCount => managing lifetime")
        manageLifetime()
        // Allow clients to rebind, in which case onRebind will be called.
        return true
    }


    private var isWorking: Boolean = true
    fun setWorking(value: Boolean) {
        isWorking = value
    }

    abstract fun buildNotification(): Notification
    private fun manageLifetime() {
        when {
            bindCount > 0 -> {
                Timber.d("stop notification and continue service")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    stopForeground(STOP_FOREGROUND_REMOVE)
                } else {
                    stopForeground(true)
                }

            }

            isWorking -> {
                Timber.d("start notification and continue service")
                startForeground(notificationId, buildNotification())
            }

            else -> {
                Timber.d("stop service")
                stopSelf()
            }
        }
    }


}