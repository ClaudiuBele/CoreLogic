package dk.sidereal.corelogic.platform.lifecycle

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import dk.sidereal.corelogic.R
import dk.sidereal.corelogic.kotlin.ext.simpleTagName
import dk.sidereal.corelogic.platform.vm.ViewModelActivityController

/** Base fragment to be used with [CoreActivity], although it is compatible with non-[CoreActivity] subclasses aswell.
 *
 */
open class CoreFragment : DialogFragment() {

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

    protected lateinit var root: FrameLayout
    protected lateinit var viewContainer: FrameLayout
    protected lateinit var overlayChildContainer: FrameLayout

    protected val TAG by lazy { javaClass.simpleTagName() }

    private val controllers: MutableList<FragmentController> = mutableListOf()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        onCreateControllers(controllers)
        Log.d(TAG, "onAttach")
        controllers.forEach { it.onAttach(context) }
    }

    /** Override [onCreateFragmentView] instead for your view, and your view will be added to [R.id.view_container].
     * Below the fragment view, you have access to [overlayChildContainer] which can be used for adding views on top of
     * the main layout without hassle
     *
     */
    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootLayout = inflater.inflate(R.layout.fragment_core, container, false)
        root = rootLayout.findViewById(R.id.item_view)
        viewContainer = rootLayout.findViewById(R.id.view_container)
        overlayChildContainer = rootLayout.findViewById(R.id.overlay_child_container)
        val innerView = onCreateFragmentView(inflater,viewContainer, savedInstanceState)
        innerView?.let {
            viewContainer.addView(it)
        }
        return rootLayout
    }

    /** Called by [onCreateView]. View returned will be added to [dk.sidereal.corelogic.R.id.overlay_container]
     *
     */
    protected open fun onCreateFragmentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = null


    /** Called in [CoreActivity.onNavigateUp]
     *
     */
    protected open fun onNavigateUp(): Boolean {
        var handledNavigateUp = false
        controllers.forEach {
            if (!handledNavigateUp) {
                handledNavigateUp = handledNavigateUp or it.onNavigateUp()
            }
        }
        return handledNavigateUp
    }

    /** Called in [CoreFragment.onAttach]. Add your outControllers to the passed parameter
     */
    protected open fun onCreateControllers(outControllers: MutableList<FragmentController>) {}


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
     * classes and constructors for them, check [ViewModelActivityController]
     *
     * Will throw exception if fragment detached and [getActivity] null
     *
     */
    fun <T : ViewModel> getVm(clazz: Class<T>): T {
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