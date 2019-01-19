package dk.sidereal.corelogic.di.lifecycle

import dk.sidereal.corelogic.platform.lifecycle.CoreApplication

abstract class DiApplication : CoreApplication() {

    internal abstract fun getDiController(): DiApplicationController<DiApplication>

    override fun onCreate() {
        super.onCreate()
        onInject()
        onPostInject()
    }

    /** Performs the injection
     *
     */
    protected open fun onInject() {
        getDiController().inject(this)
    }


    protected open fun onPostInject() {}

}