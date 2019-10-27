package dk.sidereal.corelogic.platform.lifecycle

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dk.sidereal.corelogic.platform.ControllerHolder

abstract class CoreService : Service(), ControllerHolder<ServiceController> {

    val coreApplication: CoreApplication? by lazy { application as? CoreApplication }

    /** Will throw if application is not of type [CoreApplication]
     */
    val requireCoreApplication: CoreApplication by lazy { application as CoreApplication }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        onCreateControllers()
        mutableControllers.forEach { it.onCreate() }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mutableControllers.forEach { it.onStartCommand(intent, flags, startId) }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        mutableControllers.forEach { it.onTaskRemoved(rootIntent) }
    }

    override fun onDestroy() {
        super.onDestroy()
        mutableControllers.forEach { it.onDestroy() }
    }


}