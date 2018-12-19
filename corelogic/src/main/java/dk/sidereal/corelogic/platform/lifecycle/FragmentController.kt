package dk.sidereal.corelogic.platform.lifecycle

import android.content.Context

/** A fragment controller. Must be created in onAttach and added to [BaseFragment.controllers]
 */
open class FragmentController(protected val baseFragment: BaseFragment) {
    protected val context = baseFragment.context
    protected val requireContext = baseFragment.requireContext()
    protected val baseActivity = baseFragment.baseActivity
    protected val requireBaseActivity = baseFragment.requireBaseActivity

    /** Called in [BaseFragment.onCreate] after [BaseFragment.onCreateControllers]
     */
    internal open fun onAttach(context: Context?) {}

}