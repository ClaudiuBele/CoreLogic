package dk.sidereal.corelogic.platform.vm

import android.os.Bundle
import androidx.lifecycle.ViewModel
import dk.sidereal.corelogic.kotlin.ext.simpleTagName
import dk.sidereal.corelogic.platform.lifecycle.BaseActivity

/** Viewmodel whose [restoreState] and [saveInstanceState] are called by [ViewModelActivityController] when used
 * in [BaseActivity].
 *
 * If you want to use [BaseActivity]/ [dk.sidereal.corelogic.platform.lifecycle.BaseApplication] / [Context] constructors
 * with the viewmodel without any hassles while still keeping a custom implementation, you can use [StatefulViewModelFactory]
 * which supports those constructors and additional [ViewModel] ones.
 *
 */
abstract class StatefulViewModel : ViewModel() {

    protected val TAG by lazy { javaClass.simpleTagName() }

    companion object {
        val INNER_TAG by lazy { BaseActivity::class.simpleTagName() }
    }

    abstract fun restoreState(state: Bundle?)

    abstract fun saveInstanceState(outState: Bundle)

}