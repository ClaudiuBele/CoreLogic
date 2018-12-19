package dk.sidereal.corelogic.app

import dk.sidereal.corelogic.nav.BaseNavHostFragment
import dk.sidereal.corelogic.nav.NavActivityController
import dk.sidereal.corelogic.platform.lifecycle.BaseActivity

class MainActivityController(baseActivity: BaseActivity)
    : NavActivityController(baseActivity) {

    override fun getNavHostFragment(): BaseNavHostFragment = BaseNavHostFragment.create(R.navigation.nav_main)

    override fun getBottomNavigationMenuId(): Int? {
        return null
    }

    override fun getNavigationMenuId(): Int? {
        return R.menu.menu_main
    }

    override fun getStartDestinations(): List<Int> {
        return listOf(R.id.contactsFragment, R.id.infoFragment)
    }

    override fun showActionBar(): Boolean {
        return true
    }

}