package dk.sidereal.corelogic.nav

import android.content.Context
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dk.sidereal.corelogic.platform.lifecycle.CoreFragment

/** Base fragment to be used for navigation within [CoreNavActivity].
 *
 * These fragments will crash if
 * 1. [requireNavActivity] is called with an activity that doesn't subclass [CoreNavActivity].
 * 2. [requireNavActivityController] is called while the hosting activity doesn't contain [CoreNavActivityController]
 * 2. [requireNavActivityOverlayContainer] is called in a fragment where both 1. and 2. are false
 *
 */
open class NavFragment : CoreFragment() {

    companion object {
        const val DEFAULT_SHOWS_ACTION_BAR = true
        const val DEFAULT_SHOWS_BOTTOM_NAVIGATION_VIEW = true
    }

    /** Not null from [onAttach] if used with [CoreNavActivity] and [CoreNavHostFragment]
     *
     */
    protected val navController: NavController
        get() = NavHostFragment.findNavController(this)

    /** Not null from [onAttach] if used with [CoreNavActivity] and [CoreNavHostFragment]
     *
     */
    protected val navController2: NavController
        get()  {
            if(navActivityController?.navController != null) {
                return navActivityController?.navController!!
            }
            var navController : NavController? = null;

            try{
                navController =  NavHostFragment.findNavController(this)
            } catch (ex: IllegalStateException) {
                // no nav controller in fragment
            }
            return navController!!
        }

    protected val navActivity: CoreNavActivity?
        get() = coreActivity as? CoreNavActivity

    protected val requireNavActivity: CoreNavActivity?
        get() = navActivity!!

    protected val navActivityController: CoreNavActivityController?
        get() = coreActivity?.getController(CoreNavActivityController::class.java)

    protected val requireNavActivityController: CoreNavActivityController
        get() = navActivityController!!

    open val showsActionBarOnNavigated: Boolean = DEFAULT_SHOWS_ACTION_BAR

    open val showsBottomNavigationViewOnNavigated: Boolean = DEFAULT_SHOWS_BOTTOM_NAVIGATION_VIEW

    open val animatesNavigationViewTransition: Boolean = true

    val navActivityOverlayContainer: ViewGroup?
        get() = navActivity?.overlayContainer
            ?: navActivityController?.overlayContainer

    val requireNavActivityOverlayContainer: ViewGroup
        get() = navActivityOverlayContainer!!

    /** Nav controller not null here. Becomes not null after [CoreNavHostFragment.onViewCreated], after which the home
     * fragment is created.
     *
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    open fun onAttachedToNavController(navActivityController: CoreNavActivityController) {}

}