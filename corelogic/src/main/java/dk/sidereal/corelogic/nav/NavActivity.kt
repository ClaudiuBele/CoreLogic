package dk.sidereal.corelogic.nav

import android.os.Bundle
import android.view.MenuItem
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


    lateinit var root: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var contentRoot: ConstraintLayout
    lateinit var bottomNavigationView: BottomNavigationView

    override fun setNavContentView() {
        setContentView(R.layout.activity_nav)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root = findViewById(R.id.root)
        navigationView = findViewById(R.id.navigation_view)
        contentRoot = findViewById(R.id.content_root)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        NavigationUI.onNavDestinationSelected(item!!, navController)
        return super.onOptionsItemSelected(item)
    }

    override fun onNavControllerReady(navController: NavController) {
        super.onNavControllerReady(navController)

        // only setup action bar connection with navigation if configuration is not null
        getAppBarConfiguration()?.let {
            NavigationUI.setupActionBarWithNavController(this, navController, it)
        }
        // with [BottomNavigationView]
        //NavigationUI.setupWithNavController(bottomNavView, navController)
        invalidateBottomNavigationView()
        invalidateNavigationView()
    }
    // works with getSupportActionBar
    open fun getAppBarConfiguration(): AppBarConfiguration? {
        return null

        // use something like this to specify which destinations to count as top components (so no up arrow)
        // val appBarConfiguration = AppBarConfiguration(setOf(R.id.main, R.id.android)) // list of destinations inside
    }

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
        root.closeDrawers()
        val navigationMenuId = getNavigationMenuId()
        navigationView.isEnabled = navigationMenuId != null
        if(navigationMenuId != null) {
            root.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            navigationView.inflateMenu(navigationMenuId)
            NavigationUI.setupWithNavController(navigationView, navController)
        } else {
            root.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    /** Return not null to show bottom navigation menu
     */
    open fun getBottomNavigationMenuId(): Int? = null

    open fun getNavigationMenuId(): Int? = null
}