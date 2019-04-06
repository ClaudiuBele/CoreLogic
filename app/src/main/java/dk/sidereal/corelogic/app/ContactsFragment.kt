package dk.sidereal.corelogic.app

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import dk.sidereal.corelogic.kotlin.ext.simpleTagName
import dk.sidereal.corelogic.nav.NavFragment

/**
 * A simple [Fragment] subclass.
 *
 */
class ContactsFragment() : NavFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)


        view.findViewById<Button>(R.id.popup_toggle).setOnClickListener {

            activity?.supportFragmentManager?.apply {
                val popupFragment = findFragmentByTag(PopupFragment::class.java.simpleTagName())
                if (popupFragment != null) {
                    beginTransaction()
                        .remove(popupFragment)
                        .commit()
                } else {
                    beginTransaction()
                        .add(
                            requireNavActivityOverlayContainer.id,
                            PopupFragment(),
                            PopupFragment::class.java.simpleTagName()
                        )
                        .commit()
                }
            }
        }

        return view
    }

}
