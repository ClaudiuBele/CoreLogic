package dk.sidereal.corelogic.platform.lifecycle

import android.content.Context
import dk.sidereal.corelogic.kotlin.ext.simpleTagName

/** A fragment controller. Must be created and added to list in [CoreFragment.onCreateControllers]
 */
open class FragmentController(protected val coreFragment: CoreFragment) {

    protected val TAG by lazy { javaClass.simpleTagName() }

    companion object {
        val INNER_TAG by lazy { FragmentController::class.simpleTagName() }
    }

    protected val context: Context?
        get() = coreFragment.context
    protected val requireContext: Context
        get() = coreFragment.requireContext()
    protected val coreActivity: CoreActivity?
        get() = coreFragment.coreActivity
    protected val requireCoreActivity: CoreActivity
        get() = coreFragment.requireCoreActivity

    /** Called in [CoreFragment.onCreate] after [CoreFragment.onCreateControllers]
     */
    internal open fun onAttach(context: Context?) {}

    open fun onBackPressed(): Boolean = false

}