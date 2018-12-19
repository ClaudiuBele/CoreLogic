package dk.sidereal.corelogic.nav

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
import dk.sidereal.corelogic.platform.lifecycle.BaseActivity
import dk.sidereal.corelogic.platform.widget.constraint.applyViewConstraints

abstract class NavActivityController(baseActivity: BaseActivity) : BaseNavActivityController(baseActivity) {

    protected lateinit var drawerLayout: DrawerLayout
    protected lateinit var navigationView: NavigationView
    protected lateinit var contentRoot: ConstraintLayout
    protected lateinit var bottomNavigationView: BottomNavigationView
    protected lateinit var toolbar: Toolbar
    protected var savedAppBarConfiguration: AppBarConfiguration? = null
    protected lateinit var navigationUI: MultiStartNavigationUI

    override fun onCreateView(baseActivity: BaseActivity): Boolean {
        baseActivity.setContentView(R.layout.activity_nav)
        return true
    }

    override fun onViewCreated(baseActivity: BaseActivity) {
        super.onViewCreated(baseActivity)
        baseActivity.apply {
            drawerLayout = findViewById(R.id.root)
            navigationView = findViewById(R.id.navigation_view)
            contentRoot = findViewById(R.id.content_root)
            bottomNavigationView = findViewById(R.id.bottom_navigation)
            toolbar = findViewById(R.id.toolbar)
            toolbar.visibility = if(showActionBar()) View.VISIBLE else View.GONE
            setSupportActionBar(toolbar)
        }
    }

    override fun onNavControllerReady(navController: NavController) {
        super.onNavControllerReady(navController)
        navigationUI = MultiStartNavigationUI(getStartDestinations())
        invalidateBottomNavigationView()
        invalidateNavigationView()
    }

    override fun onBackPressed(): Boolean {
        // Optional if you want the app to close when the back button is pressed
        // on a start destination
        return navigationUI.onBackPressed(activity, navController)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        NavigationUI.onNavDestinationSelected(item!!, navController)
        return false
    }

    override fun onNavigateUp(): Boolean {
        navigationUI.navigateUp(drawerLayout, navController)
        return true
    }

    override fun getNavHostFragment(): BaseNavHostFragment {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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

            navigationUI.setupActionBarWithNavController(activity, navController, drawerLayout)
            // unlock drawer
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

            // set menu
            navigationView.inflateMenu(navigationMenuId)

            // bind navigation to selected item two ways
            NavigationUI.setupWithNavController(navigationView, navController)
        } else {
            navigationUI.setupActionBarWithNavController(activity, navController, null)

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