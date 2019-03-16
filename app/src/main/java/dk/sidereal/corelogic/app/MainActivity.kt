package dk.sidereal.corelogic.app

import dk.sidereal.corelogic.platform.lifecycle.ActivityController
import dk.sidereal.corelogic.platform.lifecycle.CoreActivity

class MainActivity : CoreActivity() {

    override fun onCreateControllers(outControllers: MutableList<ActivityController>) {
        super.onCreateControllers(outControllers)
        outControllers.add(MainActivityNavController(this))
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }

}