package dk.sidereal.corelogic.platform.lifecycle

import android.content.Context
import dk.sidereal.corelogic.kotlin.ext.simpleTagName

/** A fragment controller. Must be created in onAttach and added to [BaseFragment.controllers]
 */
open class FragmentController(protected val baseFragment: BaseFragment) {

    protected val TAG by lazy { javaClass.simpleTagName() }

    companion object {
        val INNER_TAG by lazy { FragmentController::class.simpleTagName() }
    }

    protected val context = baseFragment.context
    protected val requireContext = baseFragment.requireContext()
    protected val baseActivity = baseFragment.baseActivity
    protected val requireBaseActivity = baseFragment.requireBaseActivity

    /** Called in [BaseFragment.onCreate] after [BaseFragment.onCreateControllers]
     */
    internal open fun onAttach(context: Context?) {}

}