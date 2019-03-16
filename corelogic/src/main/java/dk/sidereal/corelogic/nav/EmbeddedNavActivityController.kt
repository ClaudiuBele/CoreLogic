package dk.sidereal.corelogic.nav

import dk.sidereal.corelogic.platform.lifecycle.CoreActivity

/** Nav
 *
 */
abstract class EmbeddedNavActivityController(coreActivity: CoreActivity) : NavActivityController(coreActivity),
    CoreNavActivityController.NavFragmentCreator,
    NavActivityController.NavFragmentNavigator {

    override val navFragmentCreator: NavFragmentCreator
        get() = this

    override val navFragmentNavigator: NavFragmentNavigator
        get() = this

}