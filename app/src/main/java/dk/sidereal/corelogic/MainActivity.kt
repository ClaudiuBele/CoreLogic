package dk.sidereal.corelogic

import android.net.Uri
import android.widget.Toast
import dk.sidereal.corelogic.nav.BaseNavHostFragment
import dk.sidereal.corelogic.nav.NavActivity

class MainActivity : NavActivity(),
    IntroFragment.OnFragmentInteractionListener,
    HelloFragment.OnFragmentInteractionListener,
ShareekFragment.OnFragmentInteractionListener{

    override fun getNavHostFragment(): BaseNavHostFragment = BaseNavHostFragment.create(R.navigation.nav_main)

    // INTRO FRAGMENT
    override fun onButtonClicked() {
        navController.navigate(R.id.action_introFragment_to_helloFragment)
//        internalNavController.navigate(R.id.action_helloFragment_to_shareekFragment2)

    }

    // HELLO FRAGMENT
    override fun onFragmentInteraction(uri: Uri) {
        Toast.makeText(this, "Shareeeek", Toast.LENGTH_SHORT).show()
        navController.navigate(R.id.action_helloFragment_to_shareekFragment2)

    }
}