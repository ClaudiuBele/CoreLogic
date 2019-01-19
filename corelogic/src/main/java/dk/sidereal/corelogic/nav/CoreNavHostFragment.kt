package dk.sidereal.corelogic.nav

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.NavigationRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import dk.sidereal.corelogic.kotlin.ext.simpleTagName
import dk.sidereal.corelogic.platform.lifecycle.CoreActivity
import dk.sidereal.corelogic.platform.lifecycle.CoreApplication

/** [NavHostFragment] alternative to be used when your activity is [CoreNavActivity].
 * Fragments inside must be [NavFragment]
 */
class CoreNavHostFragment : NavHostFragment() {

    protected val TAG by lazy { javaClass.simpleTagName() }

    val coreActivity: CoreActivity?
        get() = activity as? CoreActivity
    val coreApplication: CoreApplication?
        get() = coreActivity?.coreApplication
    val requireCoreActivity: CoreActivity
        get() = requireActivity() as CoreActivity
    val requireApplication: CoreApplication
        get() = requireCoreActivity.coreApplication

    companion object {
        val TAG = "NAV"


        private val KEY_GRAPH_ID = "android-support-nav:fragment:graphId"
        private val KEY_START_DESTINATION_ARGS = "android-support-nav:fragment:startDestinationArgs"
        private val KEY_NAV_CONTROLLER_STATE = "android-support-nav:fragment:navControllerState"
        private val KEY_DEFAULT_NAV_HOST = "android-support-nav:fragment:defaultHost"

        fun create(@NavigationRes graphResId: Int): CoreNavHostFragment {
            return create(graphResId, null)
        }

        /**
         * Create a new CoreNavHostFragment instance with an inflated [NavGraph] resource.
         *
         * @param graphResId resource id of the navigation graph to inflate
         * @param startDestinationArgs arguments to send to the start destination of the graph
         * @return a new NavHostFragment instance
         */
        fun create(
            @NavigationRes graphResId: Int, startDestinationArgs: Bundle?
        ): CoreNavHostFragment {
            var b: Bundle? = null
            if (graphResId != 0) {
                b = Bundle()
                b.putInt(KEY_GRAPH_ID, graphResId)
            }
            if (startDestinationArgs != null) {
                if (b == null) {
                    b = Bundle()
                }
                b.putBundle(KEY_START_DESTINATION_ARGS, startDestinationArgs)
            }

            val result = CoreNavHostFragment()
            if (b != null) {
                result.arguments = b
            }
            return result
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }

    /** After [onViewCreated], nav controller is not null.Root [NavFragment] in the navigation flow's onCreate
     * will be called after
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        val controller = view.findNavController()

        val navActivityController = requireCoreActivity.getController(CoreNavActivityController::class.java)
            ?: throw IllegalStateException("$TAG: Can't use CoreNavHostFragment in a CoreActivity without a CoreNavActivityController controller")
        navActivityController.onNavControllerReady(controller)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        fragment?.let {
            Log.d(TAG, "onAttachFragment: ${it.javaClass.simpleTagName()}")
        }
    }
}