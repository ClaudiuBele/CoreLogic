package dk.sidereal.corelogic.platform

import androidx.annotation.CallSuper
import dk.sidereal.corelogic.platform.lifecycle.CoreActivity
import dk.sidereal.corelogic.platform.lifecycle.CoreFragment

interface ControllerHolder<T : AndroidModelController<*>> {

    var mutableControllers: MutableList<T>

    fun onCreateControllers() {
        onCreateControllers(mutableControllers)
    }

    /** Called in [CoreFragment.onAttach]. Add your outControllers to the passed parameter
     *
     * Where you setup your [ActivityController]. called in [onCreate]. Add your outControllers to [outControllers] parameter.
     * Some default outControllers are added by [CoreActivity] [dk.sidereal.corelogic.nav.CoreNavActivity]
     */
    @CallSuper
    fun onCreateControllers(outControllers: MutableList<T>) {
    }

    /** Returns a read-only list of controllers
     *
     */
    fun getControllers(): List<T> = mutableControllers.toList()

    @Suppress("UNCHECKED_CAST")
    fun <T : AndroidModelController<*>> getController(clazz: Class<T>): T? =
        mutableControllers.firstOrNull { clazz.isAssignableFrom(it.javaClass) } as? T


}