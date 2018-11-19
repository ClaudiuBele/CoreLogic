package dk.sidereal.corelogic.platform.lifecycle

import android.app.Application

open class BaseApplication : Application() {

    internal val controllers: MutableList<ApplicationController> = mutableListOf()

    override fun onCreate() {
        super.onCreate()
        onSetupControllers()
        controllers.forEach { it.onCreate() }
    }

    internal open fun onSetupControllers() {}

    @Suppress("UNCHECKED_CAST")
    internal fun <T : ApplicationController> getController(clazz: Class<T>) : T?
            = controllers.firstOrNull { it.javaClass.isAssignableFrom(clazz) } as? T

    override fun onTerminate() {
        super.onTerminate()
        controllers.forEach { it.dispose() }
    }
}

