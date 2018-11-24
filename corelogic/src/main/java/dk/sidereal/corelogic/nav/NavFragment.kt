package dk.sidereal.corelogic.nav

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dk.sidereal.corelogic.nav.BaseNavActivity.Companion.DEBUG_TAG
import dk.sidereal.corelogic.platform.lifecycle.BaseFragment

/** Base fragment to be used for navigation
 *
 */
open class NavFragment : BaseFragment() {

    /** Not null from [onAttach] if used with [BaseNavActivity] and [BaseNavHostFragment]
     *
     */
    val  navController : NavController
        get() = NavHostFragment.findNavController(this)

    /** Nav controller not null here. Becomes not null after [BaseNavHostFragment.onViewCreated], after which the home
     * fragment is created.
     *
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d(DEBUG_TAG, "NavFragment onAttach")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(DEBUG_TAG, "NavFragment onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(DEBUG_TAG, "NavFragment onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        Log.d(DEBUG_TAG, "NavFragment onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(DEBUG_TAG, "NavFragment onResume")

    }

}