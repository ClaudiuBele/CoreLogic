package dk.sidereal.corelogic.platform.lifecycle

import android.app.Application

class BaseApplication : Application() {

    val controller = ApplicationController(this)

}