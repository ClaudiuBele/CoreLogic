package dk.sidereal.corelogic.nav

import android.os.Bundle
import android.view.MenuItem
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
        setSupportActionBar(toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        NavigationUI.onNavDestinationSelected(item!!, navController)
        return super.onOptionsItemSelected(item)
    }

    override fun onNavControllerReady(navController: NavController) {
        super.onNavControllerReady(navController)

        invalidateBottomNavigationView()
        invalidateNavigationView()

        navigationUI = MultiStartNavigationUI(getStartDestinations())
        navigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

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
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            navigationView.inflateMenu(navigationMenuId)
            NavigationUI.setupWithNavController(navigationView, navController)
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    /** Return not null to show bottom navigation menu
     */
    open fun getBottomNavigationMenuId(): Int? = null

    open fun getNavigationMenuId(): Int? = null
}