package dk.sidereal.corelogic.app


import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import dk.sidereal.corelogic.nav.NavFragment
import dk.sidereal.corelogic.platform.vm.StatefulViewModel

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.button)?.setOnClickListener {
            getVm(MoreInfoViewModel::class.java).triggerClick()
        }
        getVm(MoreInfoViewModel::class.java).clicksLiveData.observe(this, Observer { clicks ->
            if (clicks == null) return@Observer

            context?.let {
                view.findViewById<TextView>(R.id.title).text = it.getString(R.string.more_info_title, clicks)
            }
        })
    }
}

class MoreInfoViewModel() : StatefulViewModel() {

    private var clicks = 0

    val clicksLiveData = MutableLiveData<Int>()

    companion object {
        const val STATE_CLICKS = "STATE_CLICKS"
    }

    override fun onCleared() {
        super.onCleared()
    }

    override fun restoreState(state: Bundle?, timeSaved: Long?) {
        clicks = state?.getInt(STATE_CLICKS) ?: 0
        clicksLiveData.postValue(clicks)
    }

    override fun saveInstanceState(outState: Bundle) {
        outState.putInt(STATE_CLICKS, clicks)
    }

    fun triggerClick() {
        clicks++
        clicksLiveData.postValue(clicks)
    }

}
