package dk.sidereal.corelogic.platform.lifecycle

import android.app.Application
import dk.sidereal.corelogic.kotlin.ext.simpleTagName

open class BaseApplication : Application() {

    protected val TAG by lazy { javaClass.simpleTagName() }

    companion object {
        val INNER_TAG by lazy { BaseFragment::class.simpleTagName() }
    }

    internal val controllers: MutableList<ApplicationController> = mutableListOf()

    override fun onCreate() {
        super.onCreate()
        onSetupControllers()
        controllers.forEach { it.onCreate() }
    }

    internal open fun onSetupControllers() {}

    @Suppress("UNCHECKED_CAST")
    internal fun <T : ApplicationController> getController(clazz: Class<T>): T? =
        controllers.firstOrNull { it.javaClass.isAssignableFrom(clazz) } as? T

    override fun onTerminate() {
        super.onTerminate()
        controllers.forEach { it.dispose() }
    }
}

