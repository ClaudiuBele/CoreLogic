package dk.sidereal.corelogic.app.view

import dk.sidereal.corelogic.app.R
import dk.sidereal.corelogic.nav.CoreNavHostFragment
import dk.sidereal.corelogic.nav.EmbeddedNavActivityController
import dk.sidereal.corelogic.platform.lifecycle.CoreActivity

class MainActivityNavController(coreActivity: CoreActivity) : EmbeddedNavActivityController(coreActivity) {

    override fun createNavHostFragment(): CoreNavHostFragment = CoreNavHostFragment.create(R.navigation.nav_app)

    override fun getBottomNavigationMenuId(): Int? {
        return R.menu.menu_main
    }

    override fun getNavigationMenuId(): Int? {
        return R.menu.menu_main
    }

    override fun getStartDestinations(): List<Int> {
        return listOf(
            R.id.contactsFragment,
            R.id.infoFragment
        )
    }

    override fun showActionBar(): Boolean {
        return true
    }

}