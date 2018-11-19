package dk.sidereal.corelogic.platform.lifecycle

class ActivityController(protected val activity: BaseActivity)  {

    val applicationController: ApplicationController
    get() = activity.baseApplication.controller

    val context = activity

}