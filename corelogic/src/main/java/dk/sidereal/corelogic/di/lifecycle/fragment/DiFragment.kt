package dk.sidereal.corelogic.di.lifecycle.fragment

import android.content.Context
import dk.sidereal.corelogic.platform.lifecycle.BaseFragment

class DiFragment : BaseFragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        onInject()
    }

    open fun onInject() {
        onInjected()
    }

    open fun onInjected() {

    }

}