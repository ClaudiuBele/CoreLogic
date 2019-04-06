package dk.sidereal.corelogic.platform.lifecycle

import android.app.Application
import dk.sidereal.corelogic.kotlin.ext.simpleTagName
import dk.sidereal.corelogic.platform.ControllerHolder

open class CoreApplication : Application(), ControllerHolder<ApplicationController> {

    protected val TAG by lazy { javaClass.simpleTagName() }

    companion object {
        val INNER_TAG by lazy { CoreFragment::class.simpleTagName() }
    }

    override var mutableControllers: MutableList<ApplicationController> = mutableListOf()

    override fun onCreate() {
        super.onCreate()
        onCreateControllers()
        mutableControllers.forEach { it.onCreate() }
    }

    override fun onTerminate() {
        super.onTerminate()
        mutableControllers.forEach { it.onTerminate() }
    }

}

