package dk.sidereal.corelogic.platform.lifecycle

import dk.sidereal.corelogic.kotlin.ext.simpleTagName

abstract class ApplicationController(protected val coreApplication: CoreApplication) {

    protected val TAG by lazy { javaClass.simpleTagName() }

    companion object {
        val INNER_TAG by lazy { ApplicationController::class.simpleTagName() }
    }


    /** Called in [CoreApplication.onCreate] after [CoreApplication.onCreateControllers]
     */
    open fun onCreate() {}

    /** Called  in [CoreApplication.onTerminate]
     *
     */
    open fun onTerminate() {}

}