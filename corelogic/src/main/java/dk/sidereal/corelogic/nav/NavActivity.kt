package dk.sidereal.corelogic.nav

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import dk.sidereal.corelogic.R
import dk.sidereal.corelogic.platform.lifecycle.BaseActivity

/** BaseActivity that has a NavHostFragment. Must implement [getNavHostFragment]
 *
 * Following https://developer.android.com/topic/libraries/architecture/navigation/navigation-implementing
 */
abstract class NavActivity : BaseActivity() {

    companion object {
        val DEBUG_TAG = "NAV"
        val NAV_HOST_ID = R.id.nav_host_fragment
    }


    lateinit var navController: NavController
    val internalNavController: NavController?
        get() = Navigation.findNavController(this, NAV_HOST_ID)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(DEBUG_TAG, "NavActivity: onCreate")
        setContentView(R.layout.activity_nav)
        val navHostFragment = getNavHostFragment()

        // add nav fragment
        supportFragmentManager.beginTransaction().apply {
            replace(NAV_HOST_ID, navHostFragment)
            if (isPrimaryNavigation()) {
                setPrimaryNavigationFragment(navHostFragment)
            }
            commit()
        }
    }

    /** If you don't want to go the path of using a [BaseNavHostFragment], your
     * [Navigation.findNavController(this, R.id.nav_host_fragment)] [NavController] will start being not null here, where
     * [R.id.nav_host_fragment] points to a [FrameLayout] which we are passing as the id of our
     * [androidx.fragment.app.FragmentTransaction.replace] call.
     *
     */
    override fun onStart() {
        super.onStart()
        Log.d(DEBUG_TAG, "NavActivity: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(DEBUG_TAG, "NavActivity: onResume")
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        NavigationUI.onNavDestinationSelected(item!!, navController)
        return super.onOptionsItemSelected(item)
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        fragment?.let {
            Log.d(DEBUG_TAG, "NavActivity: onAttachFrament ${it.javaClass.simpleName}")
        }
    }

    /** Called from listener added through [NavController.addOnNavigatedListener]
     * to the NavController
     */
    @CallSuper
    open fun onNavigated(navController: NavController, navDestination: NavDestination) {
        Log.d(DEBUG_TAG, "NavActivity: onNavigated, destination: ${navDestination.label}")
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
    NavActivity: onNavControllerReady
    NavActivity: onNavigated, destination: fragment_intro
    NavFragment onAttach, controller null false
    NavFragment onCreate, controller null false
    BaseNavHostFragment: onStart
    NavFragment onStart
    NavActivity: onStart
    NavActivity: onResume
    BaseNavHostFragment: onResume
    NavFragment onResume
     *
     */
    @CallSuper
    open fun onNavControllerReady(navController: NavController) {
        Log.d(DEBUG_TAG, "NavActivity: onNavControllerReady")
        this.navController = navController

        // called whenever navigation destination changes
        navController.addOnNavigatedListener { controller, destination ->
            onNavigated(
                controller,
                destination
            )
        }

        // only setup action bar connection with navigation if configuration is not null
        getAppBarConfiguration()?.let {
            NavigationUI.setupActionBarWithNavController(this, navController, it)
        }
        // with [BottomNavigationView]
        //NavigationUI.setupWithNavController(bottomNavView, navController)
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

    // works with getSupportActionBar
    open fun getAppBarConfiguration(): AppBarConfiguration? {
        return null

        // use something like this to specify which destinations to count as top components (so no up arrow)
        // val appBarConfiguration = AppBarConfiguration(setOf(R.id.main, R.id.android)) // list of destinations inside
    }

    // convenience
    fun View.findNavigationController() = Navigation.findNavController(this)

}