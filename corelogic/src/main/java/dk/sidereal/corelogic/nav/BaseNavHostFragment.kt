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
import dk.sidereal.corelogic.nav.NavActivity.Companion.DEBUG_TAG

/** [NavHostFragment] alternative to be used when your activity is [NavActivity].
 * Fragments inside must be [NavFragment]
 */
class BaseNavHostFragment : NavHostFragment() {


    companion object {

        private val KEY_GRAPH_ID = "android-support-nav:fragment:graphId"
        private val KEY_START_DESTINATION_ARGS = "android-support-nav:fragment:startDestinationArgs"
        private val KEY_NAV_CONTROLLER_STATE = "android-support-nav:fragment:navControllerState"
        private val KEY_DEFAULT_NAV_HOST = "android-support-nav:fragment:defaultHost"

        fun create(@NavigationRes graphResId: Int): BaseNavHostFragment {
            return create(graphResId, null)
        }

        /**
         * Create a new BaseNavHostFragment instance with an inflated [NavGraph] resource.
         *
         * @param graphResId resource id of the navigation graph to inflate
         * @param startDestinationArgs arguments to send to the start destination of the graph
         * @return a new NavHostFragment instance
         */
        fun create(
            @NavigationRes graphResId: Int, startDestinationArgs: Bundle?
        ): BaseNavHostFragment {
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

            val result = BaseNavHostFragment()
            if (b != null) {
                result.arguments = b
            }
            return result
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(DEBUG_TAG, "BaseNavHostFragment: onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(DEBUG_TAG, "BaseNavHostFragment: onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d(DEBUG_TAG, "BaseNavHostFragment: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(DEBUG_TAG, "BaseNavHostFragment: onResume")
    }

    /** After [onViewCreated], nav controller is not null.Root [NavFragment] in the navigation flow's onCreate
     * will be called after
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(DEBUG_TAG, "BaseNavHostFragment: onViewCreated")
        val controller = view.findNavController()
        if(activity is NavActivity) {
            (activity as NavActivity).onNavControllerReady(controller)
        }
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        fragment?.let {
            Log.d(DEBUG_TAG, "BaseNavHostFragment: onAttachFrament ${it.javaClass.simpleName}")
        }
    }
}