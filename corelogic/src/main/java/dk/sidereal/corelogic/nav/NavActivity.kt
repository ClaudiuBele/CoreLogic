package dk.sidereal.corelogic.nav

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dk.sidereal.corelogic.R
import dk.sidereal.corelogic.platform.widget.constraint.applyViewConstraints

/** Subclass of [BaseNavActivity] which bundles Navigation integration with the following widgets [BottomNavigationView],
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
abstract  class NavActivity : BaseNavActivity() {


    protected lateinit var drawerLayout: DrawerLayout
    protected lateinit var navigationView: NavigationView
    protected lateinit var contentRoot: ConstraintLayout
    protected lateinit var bottomNavigationView: BottomNavigationView
    protected lateinit var toolbar: Toolbar
    protected var savedAppBarConfiguration: AppBarConfiguration? = null
    protected lateinit var navigationUI: MultiStartNavigationUI

    override fun setNavContentView() {
        setContentView(R.layout.activity_nav)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drawerLayout = findViewById(R.id.root)
        navigationView = findViewById(R.id.navigation_view)
        contentRoot = findViewById(R.id.content_root)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        toolbar = findViewById(R.id.toolbar)
        toolbar.visibility = if(showActionBar()) View.VISIBLE else View.GONE
        setSupportActionBar(toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        NavigationUI.onNavDestinationSelected(item!!, navController)
        return super.onOptionsItemSelected(item)
    }

    override fun onNavControllerReady(navController: NavController) {
        super.onNavControllerReady(navController)

        navigationUI = MultiStartNavigationUI(getStartDestinations())
        invalidateBottomNavigationView()
        invalidateNavigationView()
    }

    override fun onBackPressed() {
        // Optional if you want the app to close when the back button is pressed
        // on a start destination
        if (!navigationUI.onBackPressed(this, navController)) {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp() = navigationUI.navigateUp(drawerLayout, navController)

    /** Destinations for which toolbar shows up as home
     */
    abstract fun getStartDestinations(): List<Int>

    fun invalidateBottomNavigationView() {

        val bottomNavigationMenuId = getBottomNavigationMenuId()
        if(bottomNavigationMenuId != null) {
            contentRoot.applyViewConstraints(bottomNavigationView) {
                constraintApplier ->
                constraintApplier.disconnect(ConstraintSet.TOP)
                constraintApplier.connect(ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            }
            bottomNavigationView.inflateMenu(bottomNavigationMenuId)
            NavigationUI.setupWithNavController(bottomNavigationView, navController)
        } else {
            contentRoot.applyViewConstraints(bottomNavigationView) {
                    constraintApplier ->
                constraintApplier.disconnect(ConstraintSet.BOTTOM)
                constraintApplier.connect(ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            }
        }
    }

    fun invalidateNavigationView() {
        drawerLayout.closeDrawers()
        val navigationMenuId = getNavigationMenuId()
        navigationView.isEnabled = navigationMenuId != null
        if(navigationMenuId != null) {

            navigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
            // unlock drawer
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

            // set menu
            navigationView.inflateMenu(navigationMenuId)

            // bind navigation to selected item two ways
            NavigationUI.setupWithNavController(navigationView, navController)
        } else {
            navigationUI.setupActionBarWithNavController(this, navController, null)

            // lock menu so it can't be swiped away
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    /** Return not null to show bottom navigation menu
     */
    open fun getBottomNavigationMenuId(): Int? = null

    open fun getNavigationMenuId(): Int? = null

    /** Whether the action bar should be showed
     */
    open fun showActionBar(): Boolean = true
}