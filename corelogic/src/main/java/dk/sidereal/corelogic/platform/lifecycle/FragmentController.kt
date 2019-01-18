package dk.sidereal.corelogic.platform.lifecycle

import android.content.Context
import dk.sidereal.corelogic.kotlin.ext.simpleTagName

/** A fragment controller. Must be created and added to list in [BaseFragment.onCreateControllers]
 */
open class FragmentController(protected val baseFragment: BaseFragment) {

    protected val TAG by lazy { javaClass.simpleTagName() }

    companion object {
        val INNER_TAG by lazy { FragmentController::class.simpleTagName() }
    }

    protected val context: Context?
        get() = baseFragment.context
    protected val requireContext: Context
        get() = baseFragment.requireContext()
    protected val baseActivity: BaseActivity?
        get() = baseFragment.baseActivity
    protected val requireBaseActivity: BaseActivity
        get() = baseFragment.requireBaseActivity

    /** Called in [BaseFragment.onCreate] after [BaseFragment.onCreateControllers]
     */
    internal open fun onAttach(context: Context?) {}

    open fun onBackPressed(): Boolean = false

}