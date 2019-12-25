package dk.sidereal.corelogic.platform.lifecycle

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import dk.sidereal.corelogic.kotlin.ext.simpleTagName
import dk.sidereal.corelogic.platform.ControllerHolder
import dk.sidereal.corelogic.platform.vm.ViewModelAc

/** Base fragment to be used with [CoreActivity], although it is compatible with non-[CoreActivity] subclasses aswell.
 *
 */
open class CoreFragment : DialogFragment(), ControllerHolder<FragmentController> {

    companion object {
        val INNER_TAG by lazy { CoreFragment::class.simpleTagName() }
    }

    val application: Application?
        get() = activity?.application
    val requireApplication: Application
        get() = requireCoreActivity.application
    val coreApplication: CoreApplication?
        get() = coreActivity?.coreApplication
    /** Will throw if application is not of type [CoreApplication]
     *
     */
    val requireCoreApplication: CoreApplication
        get() = requireCoreActivity.application as CoreApplication
    // activity and requireActivity already exist
    val coreActivity: CoreActivity?
        get() = activity as? CoreActivity
    val requireCoreActivity: CoreActivity
        get() = requireActivity() as CoreActivity

    val coreFragments: List<CoreFragment>
        get() = childFragmentManager.fragments.dropWhile { it !is CoreFragment }.map { it as CoreFragment }

    protected val TAG by lazy { javaClass.simpleTagName() }

    override var mutableControllers: MutableList<FragmentController> = mutableListOf()

    //region Lifecycle
    override fun onAttach(context: Context) {
        super.onAttach(context)
        onCreateControllers()
        Log.d(TAG, "onAttach")
        mutableControllers.forEach { it.onAttach(context) }
    }

    /** Called in [CoreFragment.onAttach]. Add your outControllers to the passed parameter
     */
    override fun onCreateControllers(outControllers: MutableList<FragmentController>) {
        super.onCreateControllers(outControllers)
    }
    // endregion

    /** Called in [CoreActivity.onNavigateUp]
     *
     */
    protected open fun onNavigateUp(): Boolean {
        var handledNavigateUp = false
        mutableControllers.forEach {
            if (!handledNavigateUp) {
                handledNavigateUp = handledNavigateUp or it.onNavigateUp()
            }
        }
        return handledNavigateUp
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mutableControllers.forEach { it.onActivityResult(requestCode, resultCode, data) }
    }

    /** Called from [CoreActivity.onBackPressed]
     * if no attached [ActivityController] returns true in [ActivityController.onBackPressed]
     *
     */
    protected open fun onBackPressed(): Boolean = false

    /** Called in [CoreActivity.onDestroy]
     *
     */
    open fun onActivityDestroyed() {}


    /** Retrieves the desired view model. Will create it if neeeded. For supported viewmodel
     * classes and constructors for them, check [ViewModelAc]
     *
     * Will throw exception if fragment detached and [getActivity] null
     *
     */
    fun <T : ViewModel> getVm(clazz: Class<T>): T {
        checkNotNull(coreActivity)
        val vmController = coreActivity!!.getController(ViewModelAc::class.java)
        checkNotNull(vmController)
        return vmController.get(clazz)
    }

    /** Called by [CoreActivity.onBackPressed]
     * Return true to flag that the fragment
     * handled the back internally and that the
     * activity shouldn't call super
     *
     */
    internal fun onBackPressedInternal(): Boolean {
        var handledBackPressed = false
        mutableControllers.forEach {
            if (!handledBackPressed) {
                handledBackPressed = handledBackPressed or it.onBackPressed()
            }
        }
        if (handledBackPressed) {
            return true
        }
        return onBackPressed()
    }


}