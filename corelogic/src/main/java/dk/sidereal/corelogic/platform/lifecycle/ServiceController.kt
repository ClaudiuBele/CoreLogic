package dk.sidereal.corelogic.platform.lifecycle

import android.content.Context
import android.content.Intent
import dk.sidereal.corelogic.kotlin.ext.simpleTagName
import dk.sidereal.corelogic.platform.AndroidModelController

abstract class ServiceController(override val model: CoreService) :
    AndroidModelController<CoreService> {

    protected val TAG by lazy { javaClass.simpleTagName() }

    companion object {
        val INNER_TAG by lazy { ServiceController::class.simpleTagName() }
    }

    // region Properties

    val coreApplication: CoreApplication? by lazy { model.coreApplication }

    /** Will throw if application is not of type [CoreApplication]
     */
    val requireCoreApplication: CoreApplication by lazy { model.requireCoreApplication }

    protected val service: CoreService = model

    protected val context: Context = service
    // endregion Properties

    // region Lifecycle
    /** Called in [CoreService.onCreate] after
     * [CoreService.onCreateControllers]
     */
    open fun onCreate() {}


    /** Called in [CoreService.onStartCommand]
     *
     * TODO return is not yet implemented
     */
    open fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return -1
    }

    /** Called in [CoreService.onDestroy]
     *
     */
    open fun onDestroy() {}

    /** Called in [CoreService.onTaskRemoved]
     *
     */
    open fun onTaskRemoved(rootIntent: Intent?) {}
    // endregion lifecycle


}