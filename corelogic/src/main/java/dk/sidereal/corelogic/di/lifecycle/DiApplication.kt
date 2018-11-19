package dk.sidereal.corelogic.di.lifecycle

import android.app.Application
import dk.sidereal.corelogic.platform.lifecycle.BaseApplication

abstract class DiApplication : BaseApplication() {

    interface DiComponent

    override fun onCreate() {
        super.onCreate()
        onInject()
        onPostInject()
    }

    /** Performs the injection
     *
     */
    protected  open fun onInject() {
        getDiController().inject(this)
    }
    protected open fun onPostInject() {}
    abstract fun getDiController() : DiApplicationController<DiApplication>

}