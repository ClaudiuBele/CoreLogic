package dk.sidereal.corelogic.nav

import android.util.Log
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import dk.sidereal.corelogic.R
import dk.sidereal.corelogic.platform.lifecycle.ActivityController
import dk.sidereal.corelogic.platform.lifecycle.CoreActivity

abstract class CoreNavActivityController(coreActivity: CoreActivity) : ActivityController(coreActivity) {

    companion object {

        val NAV_HOST_ROOT_ID = R.id.nav_host_fragment_root

    }

    lateinit var navController: NavController
    lateinit var navHostRoot: ViewGroup

    override fun onCreateView(coreActivity: CoreActivity): Boolean {
        coreActivity.setContentView(R.layout.activity_nav_core)
        return true
    }

    override fun onViewCreated(coreActivity: CoreActivity) {
        super.onViewCreated(coreActivity)
        // drawerLayout view must not be null
        navHostRoot = coreActivity.findViewById<ViewGroup>(NAV_HOST_ROOT_ID)!!

        val navHostFragment = getNavHostFragment()
        // add nav fragment
        coreActivity.supportFragmentManager.beginTransaction().apply {
            replace(NAV_HOST_ROOT_ID, navHostFragment)
            if (isPrimaryNavigation()) {
                setPrimaryNavigationFragment(navHostFragment)
            }
            commit()
        }

    }

    /** Called by [CoreNavHostFragment.onViewCreated] after whichs' super creates the [NavController] which is passed
     * into the activity. If you want to remove this, expect to be able to call [Navigation.findNavController]
     * and not get an exception beggining with onStart.
     *
     * The benefit of this callback is that it is called before the drawerLayout navigation's fragment's [NavFragment.onAttach]
     * is called, which means they can access the activity's nav controller if they want to.
     *
     * CoreNavHostFragment: onCreate
    CoreNavHostFragment: onViewCreated
    CoreNavActivity: onNavControllerReady
    CoreNavActivity: onDestinationChanged, destination: fragment_intro
    NavFragment onAttach, controller null false
    NavFragment onCreate, controller null false
    CoreNavHostFragment: onStart
    NavFragment onStart
    CoreNavActivity: onStart
    CoreNavActivity: onResume
    CoreNavHostFragment: onResume
    NavFragment onResume
     *
     */
    @CallSuper
    open fun onNavControllerReady(navController: NavController) {
        Log.d(TAG, "onNavControllerReady")
        this.navController = navController

        // called whenever navigation destination changes
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            onDestinationChanged(
                controller,
                destination
            )
        }

    }

    /** If this returns true, the [CoreNavHostFragment] will be set as the main primary navigation fragment,
     * handling navigation callbacks like back presses.
     */
    open fun isPrimaryNavigation(): Boolean = true

    /** Called from listener added through [NavController.addOnNavigatedListener]
     * to the NavController
     */
    @CallSuper
    open fun onDestinationChanged(navController: NavController, navDestination: NavDestination) {
        Log.d(TAG, "onDestinationChanged, destination: ${navDestination.label}")
    }

    /** Abstract function that must be implemented. Create your own fragment using a nav resource like this
     *  [CoreNavHostFragment.create(R.navigation.nav_main)]
     *
     */
    abstract fun getNavHostFragment(): CoreNavHostFragment

}