package dk.sidereal.corelogic.di

import dk.sidereal.corelogic.di.lifecycle.DiController

/** Empty interface designating a depedency Injection component. Might be created by an application, activity or fragment
 * or [dk.sidereal.corelogic.platform.lifecycle.ActivityController] or [dk.sidereal.corelogic.platform.lifecycle.ApplicationController]
 * or fragment variant
 *
 */
interface DiComponent {

    fun inject(diController: DiController)
}