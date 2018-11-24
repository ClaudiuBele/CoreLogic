package dk.sidereal.corelogic.nav

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import dk.sidereal.corelogic.R
import dk.sidereal.corelogic.platform.lifecycle.BaseActivity

/** BaseActivity that has a NavHostFragment. Must implement [getNavHostFragment].
 *
 * If you are extending this, you can implement a custom layout by overriding [setNavContentView]
 *
 * Following https://developer.android.com/topic/libraries/architecture/navigation/navigation-implementing
 */
abstract class BaseNavActivity : BaseActivity() {

    companion object {
        val DEBUG_TAG = "NAV"
        val NAV_HOST_ROOT_ID = R.id.nav_host_fragment_root
    }

    lateinit var navController: NavController
    lateinit var navHostRoot: ViewGroup

    /** Don't call [setContentView] here. If you want a custom layout override
     * [setNavContentView]
     */
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(DEBUG_TAG, "BaseNavActivity: onCreate")

        // set content view, you can override it
        setNavContentView()

        // root view must not be null
        navHostRoot = findViewById<ViewGroup>(NAV_HOST_ROOT_ID)!!

        val navHostFragment = getNavHostFragment()
        // add nav fragment
        supportFragmentManager.beginTransaction().apply {
            replace(NAV_HOST_ROOT_ID, navHostFragment)
            if (isPrimaryNavigation()) {
                setPrimaryNavigationFragment(navHostFragment)
            }
            commit()
        }
    }

    /** If you don't want to go the path of using a [BaseNavHostFragment], your
     * [Navigation.findNavController(this, R.id.nav_host_fragment)] [NavController] will start being not null here, where
     * [R.id.nav_host_fragment_root] points to a [FrameLayout] which we are passing as the id of our
     * [androidx.fragment.app.FragmentTransaction.replace] call.
     *
     */
    override fun onStart() {
        super.onStart()
        Log.d(DEBUG_TAG, "BaseNavActivity: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(DEBUG_TAG, "BaseNavActivity: onResume")
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment_root).navigateUp()

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        fragment?.let {
            Log.d(DEBUG_TAG, "BaseNavActivity: onAttachFrament ${it.javaClass.simpleName}")
        }
    }

    /** Override to set a custom layout to the activity. Must call
     * [setContentView] and must contain a Viewgroup with it [NAV_HOST_ROOT_ID] which will
     * be used to host the [BaseNavHostFragment] (happens automatically)
     */
    open fun setNavContentView() = setContentView(R.layout.activity_nav_base)

    /** Called from listener added through [NavController.addOnNavigatedListener]
     * to the NavController
     */
    @CallSuper
    open fun onNavigated(navController: NavController, navDestination: NavDestination) {
        Log.d(DEBUG_TAG, "BaseNavActivity: onNavigated, destination: ${navDestination.label}")
    }

    /** Called by [BaseNavHostFragment.onViewCreated] after whichs' super creates the [NavController] which is passed
     * into the activity. If you want to remove this, expect to be able to call [Navigation.findNavController]
     * and not get an exception beggining with onStart.
     *
     * The benefit of this callback is that it is called before the root navigation's fragment's [NavFragment.onAttach]
     * is called, which means they can access the activity's nav controller if they want to.
     *
     * BaseNavHostFragment: onCreate
    BaseNavHostFragment: onViewCreated
    BaseNavActivity: onNavControllerReady
    BaseNavActivity: onNavigated, destination: fragment_intro
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
        Log.d(DEBUG_TAG, "BaseNavActivity: onNavControllerReady")
        this.navController = navController

        // called whenever navigation destination changes
        navController.addOnNavigatedListener { controller, destination ->
            onNavigated(
                controller,
                destination
            )
        }

    }

    /** Abstract function that must be implemented. Create your own fragment using a nav resource like this
     *  [BaseNavHostFragment.create(R.navigation.nav_main)]
     *
     */
    abstract fun getNavHostFragment(): BaseNavHostFragment

    /** If this returns true, the [BaseNavHostFragment] will be set as the main primary navigation fragment,
     * handling navigation callbacks like back presses.
     */
    open fun isPrimaryNavigation(): Boolean = true

    // convenience
    fun View.findNavigationController() = Navigation.findNavController(this)

}