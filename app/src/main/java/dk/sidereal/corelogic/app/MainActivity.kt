package dk.sidereal.corelogic.app

import dk.sidereal.corelogic.platform.lifecycle.ActivityController
import dk.sidereal.corelogic.platform.lifecycle.CoreActivity

class MainActivity : CoreActivity() {

    override fun onCreateControllers(controllers: MutableList<ActivityController>) {
        super.onCreateControllers(controllers)
        controllers.add(MainActivityNavController(this))
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }

}