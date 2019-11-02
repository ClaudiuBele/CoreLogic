package dk.sidereal.corelogic.platform.lifecycle

import android.view.View
import dk.sidereal.corelogic.platform.AndroidModelController

abstract class ViewController(override val model: View) : AndroidModelController<View> {

    fun onCreate() {}

    fun onViewCreated() {}

    fun onDetach() {}

    fun onDestroy() {}

}