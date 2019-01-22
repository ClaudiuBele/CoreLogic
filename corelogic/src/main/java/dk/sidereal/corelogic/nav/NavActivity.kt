package dk.sidereal.corelogic.nav

import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

/** Subclass of [CoreNavActivity] which bundles Navigation integration with the following widgets [BottomNavigationView],
 * [NavigationView], [Toolbar].
 *
 * You need to implement [getStartDestinations] to set which destinations will count as home in relation to [Toolbar.getNavigationIcon]],
 * [onBackPressed], [onNavigateUp]
 *
 * You can override [getNavigationMenuId] and returns a non-null id to have a navigation view swipeable from the start
 * side. The view is setup with [navController] automatically
 *
 * You can override [showActionBar] to returns true or false based on whether you want the built-in [Toolbar]
 * to be shown and used for navigation
 *
 * You can override [getBottomNavigationMenuId] and return a non-null [androidx.annotation.MenuRes] id to show a
 * [BottomNavigationView] with current theme applied. The view is setup with [navController] automatically
 */
abstract class NavActivity : CoreNavActivity() {

    override fun onCreateNavigationController(): CoreNavActivityController {
        return object : NavActivityController(this@NavActivity) {
            override fun getStartDestinations(): List<Int> {
                return this@NavActivity.getStartDestinations()
            }

            override fun getNavHostFragment(): CoreNavHostFragment {
                return this@NavActivity.getNavHostFragment()
            }
        }
    }

    /** Destinations for which toolbar shows up as home
     */
    abstract fun getStartDestinations(): List<Int>
}