package dk.sidereal.corelogic.nav

import android.util.Log
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import dk.sidereal.corelogic.R
import dk.sidereal.corelogic.platform.lifecycle.ActivityController
import dk.sidereal.corelogic.platform.lifecycle.CoreActivity
import dk.sidereal.corelogic.platform.lifecycle.CoreFragment
import java.lang.ref.WeakReference

abstract class CoreNavActivityController(coreActivity: CoreActivity) : ActivityController(coreActivity) {

    companion object {

        val NAV_HOST_ROOT_ID = R.id.nav_host_fragment_root
        val NAV_HOST_FRAGMENT_TAG = "NAV_HOST_FRAGMENT_TAG"
    }

    lateinit var navController: NavController
    lateinit var navHostRoot: ViewGroup

    var lastNavigatedFragment: WeakReference<CoreFragment?> = WeakReference(CoreFragment())


    override fun onCreateView(coreActivity: CoreActivity): Boolean {
        coreActivity.setContentView(R.layout.activity_nav_core)
        return true
    }

    override fun onViewCreated(coreActivity: CoreActivity) {
        super.onViewCreated(coreActivity)
        // drawerLayout view must not be null
        navHostRoot = coreActivity.findViewById<ViewGroup>(NAV_HOST_ROOT_ID)!!


        if (coreActivity.supportFragmentManager.findFragmentByTag(NAV_HOST_FRAGMENT_TAG) == null) {
            val navHostFragment = getNavHostFragment()
            // add nav fragment
            coreActivity.supportFragmentManager.beginTransaction().apply {
                replace(NAV_HOST_ROOT_ID, navHostFragment, NAV_HOST_FRAGMENT_TAG)
                if (isPrimaryNavigation()) {
                    setPrimaryNavigationFragment(navHostFragment)
                }
                commit()
            }
        }


    }

    /** Called by [CoreNavHostFragment.onViewCreated] after whichs' super creates the [NavController] which is passed
     * into the activity. If you want to remove this, expect to be able to call [Navigation.findNavController]
     * and not getVm an exception beggining with onStart.
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
    open fun onNavControllerReady(
        navController: NavController,
        navHostFragment: CoreNavHostFragment
    ) {
        Log.d(TAG, "onNavControllerReady")
        this.navController = navController

        val fragments = navHostFragment.childFragmentManager.fragments
        val lastFragment = fragments.lastOrNull()
        val currentFragment = lastFragment as? CoreFragment

        currentFragment?.let {
            onFragmentNavigatedTo(it)
        }

        // called whenever navigation destination changes
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            onDestinationChanged(
                controller,
                destination
            )
            navHostFragment.childFragmentManager.addOnBackStackChangedListener(
                object : FragmentManager.OnBackStackChangedListener {
                    override fun onBackStackChanged() {
                        Log.d(TAG, "Backstack changed :*")

                        val fragments = navHostFragment.childFragmentManager.fragments
                        val lastFragment = fragments.lastOrNull()
                        val currentFragment = lastFragment as? CoreFragment

                        if (lastNavigatedFragment.get() != currentFragment) {
                            lastNavigatedFragment = WeakReference(currentFragment)
                            if (currentFragment != null) {
                                onFragmentNavigatedTo(currentFragment)

                            }
                        }
                        navHostFragment.childFragmentManager.removeOnBackStackChangedListener(this)
                    }
                }
            )
        }

    }

    /** Called by  [CoreNavHostFragment.onAttachFragment]
     */
    open fun onNavFragmentAttached(navFragment: NavFragment) {
        navFragment.onAttachedToNavController(this)

        if (lastNavigatedFragment.get() != navFragment) {
            lastNavigatedFragment = WeakReference(navFragment)
            onFragmentNavigatedTo(navFragment)
        }
    }

    open fun onFragmentNavigatedTo(coreFragment: CoreFragment) {

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