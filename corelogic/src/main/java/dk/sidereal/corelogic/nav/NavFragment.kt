package dk.sidereal.corelogic.nav

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dk.sidereal.corelogic.platform.lifecycle.CoreFragment

/** Base fragment to be used for navigation
 *
 */
open class NavFragment : CoreFragment() {

    /** Not null from [onAttach] if used with [CoreNavActivity] and [CoreNavHostFragment]
     *
     */
    val navController: NavController
        get() = NavHostFragment.findNavController(this)

    open val showsActionBarOnNavigated: Boolean = true

    open val showsBottomNavigationViewOnNavigated: Boolean = true

    open val animatesNavigationViewTransition: Boolean = true

    /** Nav controller not null here. Becomes not null after [CoreNavHostFragment.onViewCreated], after which the home
     * fragment is created.
     *
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    open fun onAttachedToNavController(navActivityController: CoreNavActivityController) {}

}