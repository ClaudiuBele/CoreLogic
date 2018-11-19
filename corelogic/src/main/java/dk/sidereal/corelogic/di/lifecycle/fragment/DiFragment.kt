package dk.sidereal.corelogic.di.lifecycle.fragment

import android.content.Context
import dk.sidereal.corelogic.platform.lifecycle.BaseFragment

@Suppress("UNUSED")
abstract class DiFragment : BaseFragment() {

    internal abstract fun getDiComponent(): DiFragmentController<DiFragment>

    /** Calls [onInject] followed by
     * [onPostInject]
     *
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        onInject()
        onPostInject()
    }

    /** Called in [onCreate]
     *
     */
    protected open fun onInject() {
        getDiComponent().inject(this, getDiComponent().getComponent())
    }

    /** Called after [onInject] in [onCreate]
     *
     */
    protected open fun onPostInject() {}

}