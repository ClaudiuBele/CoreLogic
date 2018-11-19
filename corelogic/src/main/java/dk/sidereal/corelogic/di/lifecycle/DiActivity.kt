package dk.sidereal.corelogic.di.lifecycle

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dk.sidereal.corelogic.platform.lifecycle.ActivityController
import dk.sidereal.corelogic.platform.lifecycle.BaseActivity

/** Prebuild [BaseActivity] for DI.
 *
 *
 */
abstract class DiActivity : BaseActivity() {

    abstract fun getDiComponent(): DiActivityController<DiActivity>

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        onInject()
        onPostInject()
    }

    protected open fun onInject() {
        getDiComponent().inject(this)
    }

    protected open fun onPostInject() {

    }
}