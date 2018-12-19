package dk.sidereal.corelogic.nav

import android.util.Log
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import dk.sidereal.corelogic.R
import dk.sidereal.corelogic.platform.lifecycle.ActivityController
import dk.sidereal.corelogic.platform.lifecycle.BaseActivity

abstract class BaseNavActivityController(baseActivity: BaseActivity) : ActivityController(baseActivity) {

    companion object {

        val NAV_HOST_ROOT_ID = R.id.nav_host_fragment_root

    }

    lateinit var navController: NavController
    lateinit var navHostRoot: ViewGroup

    override fun onCreateView(baseActivity: BaseActivity): Boolean {
        baseActivity.setContentView(R.layout.activity_nav_base)
        return true
    }

    override fun onViewCreated(baseActivity: BaseActivity) {
        super.onViewCreated(baseActivity)
        // drawerLayout view must not be null
        navHostRoot = baseActivity.findViewById<ViewGroup>(NAV_HOST_ROOT_ID)!!

        val navHostFragment = getNavHostFragment()
        // add nav fragment
        baseActivity.supportFragmentManager.beginTransaction().apply {
            replace(NAV_HOST_ROOT_ID, navHostFragment)
            if (isPrimaryNavigation()) {
                setPrimaryNavigationFragment(navHostFragment)
            }
            commit()
        }

    }

    /** Called by [BaseNavHostFragment.onViewCreated] after whichs' super creates the [NavController] which is passed
     * into the activity. If you want to remove this, expect to be able to call [Navigation.findNavController]
     * and not get an exception beggining with onStart.
     *
     * The benefit of this callback is that it is called before the drawerLayout navigation's fragment's [NavFragment.onAttach]
     * is called, which means they can access the activity's nav controller if they want to.
     *
     * BaseNavHostFragment: onCreate
    BaseNavHostFragment: onViewCreated
    BaseNavActivity: onNavControllerReady
    BaseNavActivity: onDestinationChanged, destination: fragment_intro
    NavFragment onAttach, controller null false
    NavFragment onCreate, controller null false
    BaseNavHostFragment: onStart
    NavFragment onStart
    BaseNavActivity: onStart
    BaseNavActivity: onResume
    BaseNavHostFragment: onResume
    NavFragment onResume
     *
     */
    @CallSuper
    open fun onNavControllerReady(navController: NavController) {
        Log.d(BaseNavActivity.DEBUG_TAG, "BaseNavActivity: onNavControllerReady")
        this.navController = navController

        // called whenever navigation destination changes
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            onDestinationChanged(
                controller,
                destination
            )
        }

    }

    /** If this returns true, the [BaseNavHostFragment] will be set as the main primary navigation fragment,
     * handling navigation callbacks like back presses.
     */
    open fun isPrimaryNavigation(): Boolean = true

    /** Called from listener added through [NavController.addOnNavigatedListener]
     * to the NavController
     */
    @CallSuper
    open fun onDestinationChanged(navController: NavController, navDestination: NavDestination) {
        Log.d(BaseNavActivity.DEBUG_TAG, "BaseNavActivity: onDestinationChanged, destination: ${navDestination.label}")
    }

    /** Abstract function that must be implemented. Create your own fragment using a nav resource like this
     *  [BaseNavHostFragment.create(R.navigation.nav_main)]
     *
     */
    abstract fun getNavHostFragment(): BaseNavHostFragment

}