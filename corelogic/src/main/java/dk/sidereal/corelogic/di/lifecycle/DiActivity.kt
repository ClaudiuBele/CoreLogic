package dk.sidereal.corelogic.di.lifecycle

import android.os.Bundle
import android.os.PersistableBundle
import dk.sidereal.corelogic.platform.lifecycle.ActivityController
import dk.sidereal.corelogic.platform.lifecycle.BaseActivity

/** Prebuild [BaseActivity] for DI.
 *
 *
 */
abstract class DiActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        onInject()
        onPostInject()
    }

    override fun onCreateControllers(controllers: MutableList<ActivityController>) {
        super.onCreateControllers(controllers)
        controllers.add(getDiController())
    }

    protected open fun onInject() {
        getDiController().inject(this)
    }

    protected open fun onPostInject() {

    }

    internal abstract fun getDiController(): DiActivityController<DiActivity>

}