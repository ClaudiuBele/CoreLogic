package dk.sidereal.corelogic

import android.net.Uri
import android.widget.Toast
import androidx.navigation.NavController
import dk.sidereal.corelogic.nav.BaseNavHostFragment
import dk.sidereal.corelogic.nav.NavActivity

class MainActivity : NavActivity(),
    IntroFragment.OnFragmentInteractionListener,
    HelloFragment.OnFragmentInteractionListener,
    ShareekFragment.OnFragmentInteractionListener {

    override fun getNavHostFragment(): BaseNavHostFragment = BaseNavHostFragment.create(R.navigation.nav_main)

    override fun getBottomNavigationMenuId(): Int? {
        return R.menu.menu_main
    }

    override fun getNavigationMenuId(): Int? {
        return R.menu.menu_main
    }

    // INTRO FRAGMENT
    override fun onButtonClicked() {
        navController.navigate(R.id.action_introFragment_to_helloFragment)
    }

    // HELLO FRAGMENT
    override fun onFragmentInteraction(uri: Uri) {
        Toast.makeText(this, "Shareeeek", Toast.LENGTH_SHORT).show()
        navController.navigate(R.id.action_helloFragment_to_shareekFragment2)

    }

    override fun onNavControllerReady(navController: NavController) {
        super.onNavControllerReady(navController)
    }
}