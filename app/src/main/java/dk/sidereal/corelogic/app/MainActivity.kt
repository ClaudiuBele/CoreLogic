package dk.sidereal.corelogic.app

import dk.sidereal.corelogic.platform.lifecycle.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreateControllers() {
        controllers.add(MainActivityNavController(this))
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }
}