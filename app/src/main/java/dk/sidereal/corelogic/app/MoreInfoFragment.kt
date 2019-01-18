package dk.sidereal.corelogic.app


import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.sidereal.corelogic.nav.NavFragment

/**
 * A simple [Fragment] subclass.
 *
 */
class MoreInfoFragment() : NavFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_info, container, false)
    }


}
