package dk.sidereal.corelogic.platform.lifecycle

import android.content.Context
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    protected val controllers: MutableList<FragmentController> = mutableListOf()

    val baseActivity: BaseActivity? = activity as? BaseActivity
    val baseApplication: BaseApplication? = baseActivity?.baseApplication
    val requireBaseActivity: BaseActivity get() = requireActivity() as BaseActivity
    val requireApplication: BaseApplication get() = requireBaseActivity.baseApplication

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        onSetupControllers()
        controllers.forEach { it.onAttach(context) }
    }

    /** Called by [BaseActivity.onBackPressed]
     * Return true to flag that the fragment
     * handled the back internally and that the
     * activity shouldn't call super
     *
     */
    internal open fun onBackPressed(): Boolean = false

    /** Called in [BaseFragment.onAttach]
     *
     */
    internal open fun onSetupControllers() {}


    /** Called in [BaseActivity.onDestroy]
     *
     */
    open fun dispose() {}

    /** Called after [BaseActivity.onAttachFragment] inside the override
     */
    open fun onAttachFragment(baseFragment: BaseFragment?) {

    }


}