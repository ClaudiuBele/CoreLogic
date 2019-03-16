package dk.sidereal.corelogic.di.lifecycle

import android.os.Bundle
import android.os.PersistableBundle
import dk.sidereal.corelogic.platform.lifecycle.ActivityController
import dk.sidereal.corelogic.platform.lifecycle.CoreActivity

/** Prebuild [CoreActivity] for DI.
 *
 *
 */
abstract class DiActivity : CoreActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        onInject()
        onPostInject()
    }

    override fun onCreateControllers(outControllers: MutableList<ActivityController>) {
        super.onCreateControllers(outControllers)
        outControllers.add(getDiController())
    }

    protected open fun onInject() {
        getDiController().inject(this)
    }

    protected open fun onPostInject() {

    }

    internal abstract fun getDiController(): DiActivityController<DiActivity>

}