package dk.sidereal.corelogic.platform.lifecycle

import android.app.Application
import dk.sidereal.corelogic.kotlin.ext.simpleTagName

open class BaseApplication : Application() {

    protected val TAG by lazy { javaClass.simpleTagName() }

    companion object {
        val INNER_TAG by lazy { BaseFragment::class.simpleTagName() }
    }

    private val controllers: MutableList<ApplicationController> = mutableListOf()

    override fun onCreate() {
        super.onCreate()
        onCreateControllers(controllers)
        controllers.forEach { it.onCreate() }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : ApplicationController> getController(clazz: Class<T>): T? =
        controllers.firstOrNull { clazz.isAssignableFrom(it.javaClass) } as? T

    override fun onTerminate() {
        super.onTerminate()
        controllers.forEach { it.onTerminate() }
    }

    /** Returns a read-only list of controllers
     *
     */
    fun getControllers(): List<ApplicationController> = controllers.toList()

    protected open fun onCreateControllers(controllers: MutableList<ApplicationController>) {}


}

