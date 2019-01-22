package dk.sidereal.corelogic.di.lifecycle

import dk.sidereal.corelogic.di.DiComponent
import dk.sidereal.corelogic.platform.lifecycle.ActivityController
import dk.sidereal.corelogic.platform.lifecycle.CoreActivity
import dk.sidereal.corelogic.platform.lifecycle.CoreFragment

abstract class DiActivityController<T : CoreActivity>(activity: T) :
    ActivityController(activity),
    DiController {

    abstract fun inject(activity: T)

    @Suppress("UNUSED")
    val applicationController: DiApplicationController<*>?
        get () = activity.coreApplication?.getController(DiApplicationController::class.java)

    @Suppress("UNUSED")
    val applicationComponent: DiComponent?
        get() = activity.coreApplication?.getController(DiApplicationController::class.java)?.getComponent()

    @Suppress("UNUSED")
    override fun onAttachFragment(coreFragment: CoreFragment?) {
        super.onAttachFragment(coreFragment)

    }

}