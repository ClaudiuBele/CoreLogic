package dk.sidereal.corelogic.di.lifecycle

import dk.sidereal.corelogic.di.DiComponent
import dk.sidereal.corelogic.platform.lifecycle.ActivityController
import dk.sidereal.corelogic.platform.lifecycle.BaseActivity
import dk.sidereal.corelogic.platform.lifecycle.BaseFragment

abstract class DiActivityController<T : BaseActivity>(activity: T) : ActivityController(activity) {

    abstract fun getComponent(): DiComponent
    abstract fun inject(activity: T)

    @Suppress("UNUSED")
    val applicationController: DiApplicationController<*>?
        get () = activity.baseApplication.getController(DiApplicationController::class.java)

    @Suppress("UNUSED")
    val applicationComponent: DiComponent?
        get() = activity.baseApplication.getController(DiApplicationController::class.java)?.getComponent()

    @Suppress("UNUSED")
    override fun onAttachFragment(baseFragment: BaseFragment?) {
        super.onAttachFragment(baseFragment)

    }

}