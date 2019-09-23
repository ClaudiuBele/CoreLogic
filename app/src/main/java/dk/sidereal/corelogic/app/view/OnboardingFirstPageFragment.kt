package dk.sidereal.corelogic.app.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import dk.sidereal.corelogic.app.R
import dk.sidereal.corelogic.nav.NavFragment


class OnboardingFirstPageFragment : NavFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding_first_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<Button>(R.id.next)
        button.setOnClickListener {
            navController.navigate(R.id.action_onboarding_1_to_2)
        }
    }

    override val showsActionBarOnNavigated: Boolean
        get() = false

    override val showsBottomNavigationViewOnNavigated: Boolean
        get() = false

}
