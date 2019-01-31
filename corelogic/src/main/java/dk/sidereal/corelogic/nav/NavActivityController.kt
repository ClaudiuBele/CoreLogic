package dk.sidereal.corelogic.nav

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isEmpty
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dk.sidereal.corelogic.R
import dk.sidereal.corelogic.platform.lifecycle.CoreActivity
import dk.sidereal.corelogic.platform.lifecycle.CoreFragment
import dk.sidereal.corelogic.platform.widget.Views
import dk.sidereal.corelogic.platform.widget.constraint.applyViewConstraints

abstract class NavActivityController(coreActivity: CoreActivity) : CoreNavActivityController(coreActivity) {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var contentRoot: ConstraintLayout
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var navHostFragmentRoot: FrameLayout
    lateinit var toolbar: Toolbar
    lateinit var appBar: AppBarLayout
    protected var savedAppBarConfiguration: AppBarConfiguration? = null
    lateinit var navigationUI: MultiStartNavigationUI

    override fun onCreateView(coreActivity: CoreActivity): Boolean {
        coreActivity.setContentView(R.layout.activity_nav)
        return true
    }

    override fun onViewCreated(coreActivity: CoreActivity) {
        super.onViewCreated(coreActivity)
        coreActivity.apply {
            drawerLayout = findViewById(R.id.drawer_layout)
            navigationView = findViewById(R.id.navigation_view)
            contentRoot = findViewById(R.id.content_root)
            bottomNavigationView = findViewById(R.id.bottom_navigation)
            navHostFragmentRoot = findViewById(R.id.nav_host_fragment_root)
            toolbar = findViewById(R.id.toolbar)
            appBar = findViewById(R.id.appbar)
            toolbar.visibility = if (showActionBar()) View.VISIBLE else View.GONE
            setSupportActionBar(toolbar)
        }
    }

    override fun onNavControllerReady(navController: NavController, navHostFragment: CoreNavHostFragment) {
        super.onNavControllerReady(navController, navHostFragment)
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
        return navigationUI.navigateUp(drawerLayout, navController)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        navController.saveState()
    }

    override fun onFragmentNavigatedTo(coreFragment: CoreFragment) {
        super.onFragmentNavigatedTo(coreFragment)


        Views.afterViewsMeasured(listOf(appBar, bottomNavigationView)) {

            val newBottomNavigationViewAlpha =
                if ((coreFragment as? NavFragment)?.showsBottomNavigationViewOnNavigated == true) 1F else 0F

            navHostFragmentRoot.setPadding(
                0,
                0,
                0,
                ((1 - newBottomNavigationViewAlpha) * bottomNavigationView.measuredHeight).toInt()
            )


            if (coreFragment is NavFragment) {
                val newAppBarAlpha = if (coreFragment.showsActionBarOnNavigated) 1F else 0F

                ValueAnimator.ofFloat(appBar.alpha, newAppBarAlpha).apply {
                    addUpdateListener {
                        val value = it.animatedValue as Float
                        appBar.alpha = value
                    }
                    duration = 1000
                    start()
                }

                ValueAnimator.ofFloat(bottomNavigationView.alpha, newBottomNavigationViewAlpha).apply {
                    addUpdateListener {
                        val value = it.animatedValue as Float
                        bottomNavigationView.alpha = value
                    }
                    duration = 1000
                    start()
                }
            }
        }
    }

    /** Destinations for which toolbar shows up as home
     */
    abstract fun getStartDestinations(): List<Int>

    fun invalidateBottomNavigationView() {

        val bottomNavigationMenuId = getBottomNavigationMenuId()
        if (bottomNavigationMenuId != null) {
            contentRoot.applyViewConstraints(bottomNavigationView) { constraintApplier ->
                constraintApplier.disconnect(ConstraintSet.TOP)
                constraintApplier.connect(ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            }
            if (bottomNavigationView.menu.isEmpty()) {
                bottomNavigationView.inflateMenu(bottomNavigationMenuId)
            }
            NavigationUI.setupWithNavController(bottomNavigationView, navController)
        } else {
            contentRoot.applyViewConstraints(bottomNavigationView) { constraintApplier ->
                constraintApplier.disconnect(ConstraintSet.BOTTOM)
                constraintApplier.connect(ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            }
        }
    }

    fun invalidateNavigationView() {
        drawerLayout.closeDrawers()
        val navigationMenuId = getNavigationMenuId()
        navigationView.isEnabled = navigationMenuId != null
        if (navigationMenuId != null) {

            navigationUI.setupActionBarWithNavController(activity, navController, drawerLayout)
            // unlock drawer
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

            // set menu
            if (navigationView.menu.isEmpty()) {
                navigationView.inflateMenu(navigationMenuId)
            }

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