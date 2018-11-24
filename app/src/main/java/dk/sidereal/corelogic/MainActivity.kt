package dk.sidereal.corelogic

import dk.sidereal.corelogic.nav.BaseNavHostFragment
import dk.sidereal.corelogic.nav.NavActivity

class MainActivity : NavActivity() {

    override fun getNavHostFragment(): BaseNavHostFragment = BaseNavHostFragment.create(R.navigation.nav_main)

    override fun getBottomNavigationMenuId(): Int? {
        return R.menu.menu_main
    }

    override fun getNavigationMenuId(): Int? {
        return R.menu.menu_main
    }

    override fun getStartDestinations(): List<Int> {
        return listOf(R.id.contactsFragment, R.id.infoFragment)
    }
}