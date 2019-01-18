package dk.sidereal.corelogic.di.lifecycle

import dk.sidereal.corelogic.platform.lifecycle.ApplicationController
import dk.sidereal.corelogic.platform.lifecycle.CoreApplication

abstract class DiApplicationController<T : CoreApplication>(application: T) :
    ApplicationController(application),
    DiController {

    abstract fun inject(t: T)

}