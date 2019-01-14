package dk.sidereal.corelogic.di.lifecycle

import dk.sidereal.corelogic.platform.lifecycle.ApplicationController
import dk.sidereal.corelogic.platform.lifecycle.BaseApplication

abstract class DiApplicationController<T : BaseApplication>(application: T) :
    ApplicationController(application),
    DiController {

    abstract fun inject(t: T)

}