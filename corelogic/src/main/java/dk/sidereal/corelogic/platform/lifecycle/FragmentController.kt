package dk.sidereal.corelogic.platform.lifecycle

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.annotation.CallSuper
import androidx.lifecycle.LifecycleObserver
import dk.sidereal.corelogic.kotlin.ext.simpleTagName
import dk.sidereal.corelogic.platform.AndroidModelController

/** Fragment controller. Contains a reference to a [CoreFragment] in order to delegate fragment callbacks ([LifecycleObserver]
 * is not enough) in isolated units of logic. Must be created in [CoreFragment.onCreateControllers]
 *
 * Subclasses should shorten [FragmentController] suffix to Fc.
 */
open class FragmentController(override val model: CoreFragment) :
    AndroidModelController<CoreFragment> {

    protected val TAG by lazy { javaClass.simpleTagName() }

    companion object {
        val INNER_TAG by lazy { FragmentController::class.simpleTagName() }
    }

    protected val coreFragment: CoreFragment = model

    protected val context: Context?
        get() = coreFragment.context
    protected val requireContext: Context
        get() = coreFragment.requireContext()
    protected val coreActivity: CoreActivity?
        get() = coreFragment.coreActivity
    protected val requireCoreActivity: CoreActivity
        get() = coreFragment.requireCoreActivity
    val requireApplication: Application
        get() = requireCoreActivity.application
    val requireCoreApplication: CoreApplication
        get() = requireCoreActivity.application as CoreApplication

    /** Called in [CoreFragment.onCreate] after [CoreFragment.onCreateControllers]
     */
    internal open fun onAttach(context: Context?) {}

    open fun onBackPressed(): Boolean = false

    /** Called in [CoreFragment.onNavigateUp]
     */
    open fun onNavigateUp(): Boolean = false

    /** Called in [CoreFragment.onActivityResult]
     *
     */
    @CallSuper
    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    /**
     *
     */
}