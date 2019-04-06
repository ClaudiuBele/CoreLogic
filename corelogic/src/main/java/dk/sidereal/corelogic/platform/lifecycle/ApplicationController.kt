package dk.sidereal.corelogic.platform.lifecycle

import dk.sidereal.corelogic.kotlin.ext.simpleTagName
import dk.sidereal.corelogic.platform.AndroidModelController

/** Application controller. Contains a reference to a [CoreApplication] in order to delegate application callbacks.
 * Must be created in [CoreApplication.onCreateControllers]
 *
 * Subclasses should shorten [ApplicationController] suffix to Apc.
 *
 */
abstract class ApplicationController(override val model: CoreApplication) :
    AndroidModelController<CoreApplication> {

    protected val TAG by lazy { javaClass.simpleTagName() }

    companion object {
        val INNER_TAG by lazy { ApplicationController::class.simpleTagName() }
    }

    protected val coreApplication: CoreApplication = model

    /** Called in [CoreApplication.onCreate] after [CoreApplication.onCreateControllers]
     */
    open fun onCreate() {}

    /** Called  in [CoreApplication.onTerminate]
     *
     */
    open fun onTerminate() {}

}