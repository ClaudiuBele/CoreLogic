package dk.sidereal.corelogic.platform.lifecycle

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import dk.sidereal.corelogic.kotlin.ext.simpleTagName
import dk.sidereal.corelogic.platform.vm.ViewModelActivityController

open class CoreFragment : Fragment() {

    protected val TAG by lazy { javaClass.simpleTagName() }

    companion object {
        val INNER_TAG by lazy { CoreFragment::class.simpleTagName() }
    }

    val coreActivity: CoreActivity?
        get() = activity as? CoreActivity
    val coreApplication: CoreApplication?
        get() = coreActivity?.coreApplication
    val requireCoreActivity: CoreActivity
        get() = requireActivity() as CoreActivity
    val requireApplication: CoreApplication
        get() = requireCoreActivity.coreApplication

    private val controllers: MutableList<FragmentController> = mutableListOf()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        onCreateControllers(controllers)
        Log.d(TAG, "onAttach")
        controllers.forEach { it.onAttach(context) }
    }


    /** Retrieves the desired view model. Will create it if neeeded. For supported viewmodel
     * classes and constructors for them, check [ViewModelActivityController]
     *
     * Will throw exception if fragment detached and [getActivity] null
     *
     */
    fun <T : ViewModel> get(clazz: Class<T>): T {
        checkNotNull(coreActivity)
        val vmController = coreActivity!!.getController(ViewModelActivityController::class.java)
        checkNotNull(vmController)
        return vmController.get(clazz)
    }


    /** Returns a read-only list of controllers
     *
     */
    fun getControllers(): List<FragmentController> = controllers.toList()

    @Suppress("UNCHECKED_CAST")
    fun <T : FragmentController> getController(clazz: Class<T>): T? =
        controllers.firstOrNull { clazz.isAssignableFrom(it.javaClass) } as? T

    /** Called in [CoreFragment.onAttach]
     *
     */
    protected open fun onCreateControllers(controllers: MutableList<FragmentController>) {}


    /** Called from [CoreActivity.onBackPressed]
     * if no attached [ActivityController] returns true in [ActivityController.onBackPressed]
     *
     */
    open fun onBackPressed(): Boolean = false

    /** Called in [CoreActivity.onDestroy]
     *
     */
    open fun dispose() {}

    /** Called after [CoreActivity.onAttachFragment] inside the override
     */
    open fun onAttachFragment(coreFragment: CoreFragment?) {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    /** Called by [CoreActivity.onBackPressed]
     * Return true to flag that the fragment
     * handled the back internally and that the
     * activity shouldn't call super
     *
     */
    internal fun onBackPressedInternal(): Boolean {
        var handledBackPressed = false
        controllers.forEach {
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