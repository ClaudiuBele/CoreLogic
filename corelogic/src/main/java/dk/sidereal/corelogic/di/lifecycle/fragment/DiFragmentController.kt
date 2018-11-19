package dk.sidereal.corelogic.di.lifecycle.fragment

import dk.sidereal.corelogic.di.lifecycle.DiActivityController
import dk.sidereal.corelogic.di.lifecycle.DiApplicationController
import dk.sidereal.corelogic.di.lifecycle.DiComponent
import dk.sidereal.corelogic.platform.lifecycle.ApplicationController
import dk.sidereal.corelogic.platform.lifecycle.BaseFragment
import dk.sidereal.corelogic.platform.lifecycle.FragmentController

@Suppress("UNUSED")
abstract class DiFragmentController<T : BaseFragment>(fragment: T) : FragmentController(fragment) {


    internal abstract fun getComponent(): DiComponent
    internal abstract fun <Component : DiComponent> inject(fragment: T, component: Component)

    /** [DiActivityController] in case our [dk.sidereal.corelogic.platform.lifecycle.BaseActivity]?
     * has it.
     */
    @Suppress("UNUSED")
    val activityController: DiActivityController<*>?
        get() = baseActivity?.getController(DiActivityController::class.java)

    /** [dk.sidereal.corelogic.platform.lifecycle.BaseActivity]?'s [DiComponent], if it has one.
     * Will be not null by default if host activity is [dk.sidereal.corelogic.di.lifecycle.DiActivity]
     */
    @Suppress("UNUSED")
    val activityComponent: DiComponent?
        get() = activityController?.getComponent()

    /** [DiApplicationController] in case our [dk.sidereal.corelogic.platform.lifecycle.BaseApplication]
     * has one. Otherwise null
     *
     */
    @Suppress("UNUSED")
    val applicationController: DiApplicationController<*>?
        get () = baseActivity?.baseApplication?.getController(DiApplicationController::class.java)

    /** [dk.sidereal.corelogic.platform.lifecycle.BaseApplication]?'s [DiComponent], if it has one.
     * Will be not null by default if host application is [dk.sidereal.corelogic.di.lifecycle.DiApplication]
     */
    @Suppress("UNUSED")
    val applicationComponent: DiComponent?
        get() = applicationController?.getComponent()

}