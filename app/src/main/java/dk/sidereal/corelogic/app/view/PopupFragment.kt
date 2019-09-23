package dk.sidereal.corelogic.app.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.sidereal.corelogic.app.R
import dk.sidereal.corelogic.nav.NavFragment

class PopupFragment : NavFragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_popup, container, false)
        return view
    }


}