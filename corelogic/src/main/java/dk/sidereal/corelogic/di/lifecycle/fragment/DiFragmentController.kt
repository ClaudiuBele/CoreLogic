package dk.sidereal.corelogic.di.lifecycle.fragment

import dk.sidereal.corelogic.di.DiComponent
import dk.sidereal.corelogic.di.lifecycle.DiActivityController
import dk.sidereal.corelogic.di.lifecycle.DiApplicationController
import dk.sidereal.corelogic.di.lifecycle.DiController
import dk.sidereal.corelogic.platform.lifecycle.CoreFragment
import dk.sidereal.corelogic.platform.lifecycle.FragmentController

@Suppress("UNUSED")
abstract class DiFragmentController<T : CoreFragment>(fragment: T) :
    FragmentController(fragment),
    DiController {

    internal abstract fun <Component : DiComponent> inject(fragment: T, component: Component)

    /** [DiActivityController] in case our [dk.sidereal.corelogic.platform.lifecycle.CoreActivity]?
     * has it.
     */
    @Suppress("UNUSED")
    val activityController: DiActivityController<*>?
        get() = coreActivity?.getController(DiActivityController::class.java)

    /** [dk.sidereal.corelogic.platform.lifecycle.CoreActivity]?'s [DiComponent], if it has one.
     * Will be not null by default if host activity is [dk.sidereal.corelogic.di.lifecycle.DiActivity]
     */
    @Suppress("UNUSED")
    val activityComponent: DiComponent?
        get() = activityController?.getComponent()

    /** [DiApplicationController] in case our [dk.sidereal.corelogic.platform.lifecycle.CoreApplication]
     * has one. Otherwise null
     *
     */
    @Suppress("UNUSED")
    val applicationController: DiApplicationController<*>?
        get () = coreActivity?.coreApplication?.getController(DiApplicationController::class.java)

    /** [dk.sidereal.corelogic.platform.lifecycle.CoreApplication]?'s [DiComponent], if it has one.
     * Will be not null by default if host application is [dk.sidereal.corelogic.di.lifecycle.DiApplication]
     */
    @Suppress("UNUSED")
    val applicationComponent: DiComponent?
        get() = applicationController?.getComponent()

}