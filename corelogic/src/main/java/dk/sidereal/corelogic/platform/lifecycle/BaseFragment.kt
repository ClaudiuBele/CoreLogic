package dk.sidereal.corelogic.platform.lifecycle

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dk.sidereal.corelogic.kotlin.ext.simpleTagName

open class BaseFragment : Fragment() {

    protected val TAG by lazy { javaClass.simpleTagName()}

    companion object {
        val INNER_TAG by lazy { BaseFragment::class.simpleTagName() }
    }

    protected val controllers: MutableList<FragmentController> = mutableListOf()

    val baseActivity: BaseActivity? = activity as? BaseActivity
    val baseApplication: BaseApplication? = baseActivity?.baseApplication
    val requireBaseActivity: BaseActivity get() = requireActivity() as BaseActivity
    val requireApplication: BaseApplication get() = requireBaseActivity.baseApplication

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        onCreateControllers()
        Log.d(TAG, "onAttach")
        controllers.forEach { it.onAttach(context) }
    }

    /** Called by [BaseActivity.onBackPressed]
     * Return true to flag that the fragment
     * handled the back internally and that the
     * activity shouldn't call super
     *
     */
    open fun onBackPressed(): Boolean = false

    /** Called in [BaseFragment.onAttach]
     *
     */
    protected open fun onCreateControllers() {}


    /** Called in [BaseActivity.onDestroy]
     *
     */
    open fun dispose() {}

    /** Called after [BaseActivity.onAttachFragment] inside the override
     */
    open fun onAttachFragment(baseFragment: BaseFragment?) {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}