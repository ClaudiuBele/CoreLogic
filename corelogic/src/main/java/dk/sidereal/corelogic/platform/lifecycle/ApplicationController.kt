package dk.sidereal.corelogic.platform.lifecycle

abstract class ApplicationController(protected val baseApplication: BaseApplication) {

    /** Called in [BaseApplication.onCreate] after [BaseApplication.onSetupControllers]
     */
    open fun onCreate() {}

    /** Called  in [BaseApplication.onTerminate]
     *
     */
    open fun dispose() {}

}