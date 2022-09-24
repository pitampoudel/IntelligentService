package pitam.intelligentservice.demo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import pitam.intelligentservice.demo.service.DemoService
import pitam.intelligentservice.demo.service.DemoServiceConnection
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @Inject
    lateinit var serviceConnection: DemoServiceConnection

    override fun onStart() {
        super.onStart()
        val serviceIntent = Intent(this, DemoService::class.java)
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
    }
}