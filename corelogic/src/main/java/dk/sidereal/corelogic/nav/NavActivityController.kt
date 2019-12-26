package dk.sidereal.corelogic.nav

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
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

    /** Delegates navigation-related information acquiring for setting up the views in the activity.
     * A bottom navigation view, navigation view, or Action bar
     *
     *  You need to implement [getStartDestinations] to set which destinations will count as home in relation to [Toolbar.getNavigationIcon]],
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
    interface NavFragmentNavigator {
        /** Return not null to show bottom navigation menu
         */
        fun getBottomNavigationMenuId(): Int? = null

        /** Returns not null to show swipeable navigation menu
         *
         */
        fun getNavigationMenuId(): Int? = null

        /** Whether the action bar should be showed
         */
        fun showActionBar(): Boolean = true

        /** Destinations for which toolbar shows up as home
         */
        fun getStartDestinations(): List<Int>
    }

    abstract val navFragmentNavigator: NavFragmentNavigator

    /** Whether to ignore back presses. Useful to set to true when working with fragments through the backstack
     */
    var ignoreBackPress: Boolean = false

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
            toolbar.visibility = if (navFragmentNavigator.showActionBar()) View.VISIBLE else View.GONE
            setSupportActionBar(toolbar)
        }
    }

    override fun onNavControllerReady(navController: NavController, navHostFragment: CoreNavHostFragment) {
        super.onNavControllerReady(navController, navHostFragment)
        navigationUI = MultiStartNavigationUI(navFragmentNavigator.getStartDestinations())
        invalidateBottomNavigationView()
        invalidateNavigationView()
    }

    override fun onBackPressed(): Boolean {
        if(ignoreBackPress)
            return false
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
                if ((coreFragment as? NavFragment)?.showsBottomNavigationViewOnNavigated
                        ?: NavFragment.DEFAULT_SHOWS_BOTTOM_NAVIGATION_VIEW) 1F else 0F

            val newAppBarAlpha = if ((coreFragment as? NavFragment)?.showsActionBarOnNavigated
                        ?: NavFragment.DEFAULT_SHOWS_ACTION_BAR) 1F else 0F


//            navHostFragmentRoot.setPadding(
//                0,
//                0,
//                0,
//                ((1 - newBottomNavigationViewAlpha) * bottomNavigationView.measuredHeight).toInt()
//            )
            ValueAnimator.ofFloat(appBar.alpha, newAppBarAlpha).apply {
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener {
                    val value = it.animatedValue as Float
                    appBar.alpha = value
                    val translateY = (1 - value) * -appBar.measuredHeight
                    appBar.translationY = translateY
                }
                duration = 500
                start()
            }

            ValueAnimator.ofFloat(bottomNavigationView.alpha, newBottomNavigationViewAlpha).apply {
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener {
                    val value = it.animatedValue as Float
                    bottomNavigationView.alpha = value
                    val translateY = (1 - value) * bottomNavigationView.measuredHeight
                    bottomNavigationView.translationY = translateY
                }
                duration = 500
                start()
            }
        }
    }

    fun invalidateBottomNavigationView() {

        val bottomNavigationMenuId = navFragmentNavigator.getBottomNavigationMenuId()
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
        val navigationMenuId = navFragmentNavigator.getNavigationMenuId()
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
}