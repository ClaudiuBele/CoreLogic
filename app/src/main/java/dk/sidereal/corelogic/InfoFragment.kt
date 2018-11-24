package dk.sidereal.corelogic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import dk.sidereal.corelogic.nav.NavFragment

class InfoFragment : NavFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onStart() {
        super.onStart()
        view?.findViewById<Button>(R.id.button)?.setOnClickListener {
            navController.navigate(R.id.action_infoFragment_to_moreInfoFragment)
        }
    }

}