package dk.sidereal.corelogic.nav

import android.util.Log
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import dk.sidereal.corelogic.R
import dk.sidereal.corelogic.kotlin.ext.simpleTagName
import dk.sidereal.corelogic.platform.lifecycle.ActivityController
import dk.sidereal.corelogic.platform.lifecycle.CoreActivity
import dk.sidereal.corelogic.platform.lifecycle.CoreFragment
import java.lang.ref.WeakReference

/** Created view must provide ViewGroup in layout from [onCreateView] at id
 * [dk.sidereal.corelogic.R.id.overlay_container]
 *
 */
abstract class CoreNavActivityController(coreActivity: CoreActivity) :
    ActivityController(coreActivity) {

    /** Delegates creation of [CoreNavHostFragment], required for it. You can create your own [NavFragmentCreator] using a nav resource,
     * or [CoreNavHostFragment.create] e.g. [CoreNavHostFragment.create(R.navigation.nav_main)]
     *
     */
    interface NavFragmentCreator {
        /** Create your own fragment using  [CoreNavHostFragment.Companion.create], using an xml nav resource, e.g.
         *  [CoreNavHostFragment.create(R.navigation.nav_main)].
         *
         *  The fragment is then added to the backstack automatically at [CoreNavActivityController.NAV_HOST_ROOT_ID],
         *  so you are only tasked with the fragment creation
         *
         *  [androidx.fragment.app.FragmentTransaction.setPrimaryNavigationFragment] is called on the transaction if
         *  [isPrimaryNavigation] returns true (true by default)
         */
        fun createNavHostFragment(): CoreNavHostFragment
    }

    companion object {

        val NAV_HOST_ROOT_ID = R.id.nav_host_fragment_root
        val NAV_HOST_FRAGMENT_TAG = "NAV_HOST_FRAGMENT_TAG"
    }

    abstract val navFragmentCreator: NavFragmentCreator

    lateinit var navController: NavController
    lateinit var navHostFragment: CoreNavHostFragment
    lateinit var navHostRoot: ViewGroup

    var lastNavigatedFragment: WeakReference<CoreFragment?> = WeakReference(CoreFragment())
    lateinit var overlayContainer: ViewGroup


    override fun onCreateView(coreActivity: CoreActivity): Boolean {
        coreActivity.setContentView(R.layout.activity_nav_core)
        return true
    }

    override fun onViewCreated(coreActivity: CoreActivity) {
        super.onViewCreated(coreActivity)
        // drawerLayout view must not be null
        navHostRoot = coreActivity.findViewById<ViewGroup>(NAV_HOST_ROOT_ID)!!
        overlayContainer = coreActivity.findViewById(R.id.overlay_container) as? ViewGroup ?:
                throw IllegalStateException("ActivityController ${this::class.java.simpleTagName()} doesn't contain Viewgroup with id R.id.overlay_container")


        if (coreActivity.supportFragmentManager.findFragmentByTag(NAV_HOST_FRAGMENT_TAG) == null) {
            val navHostFragment = navFragmentCreator.createNavHostFragment()
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
        this.navHostFragment = navHostFragment

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
}