package dk.sidereal.corelogic.platform.lifecycle

import dk.sidereal.corelogic.kotlin.ext.simpleTagName

abstract class ApplicationController(protected val baseApplication: BaseApplication) {

    protected val TAG by lazy { javaClass.simpleTagName() }

    companion object {
        val INNER_TAG by lazy { ApplicationController::class.simpleTagName() }
    }


    /** Called in [BaseApplication.onCreate] after [BaseApplication.onCreateControllers]
     */
    open fun onCreate() {}

    /** Called  in [BaseApplication.onTerminate]
     *
     */
    open fun onTerminate() {}

}