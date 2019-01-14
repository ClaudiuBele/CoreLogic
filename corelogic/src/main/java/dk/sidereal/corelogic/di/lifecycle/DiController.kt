package dk.sidereal.corelogic.di.lifecycle

import dk.sidereal.corelogic.di.DiComponent

/** Umbrella interface for [DiApplicationController], [DiActivityController] and
 *  [dk.sidereal.corelogic.di.lifecycle.fragment.DiFragmentController]
 *
 */
interface DiController {

    fun getComponent(): DiComponent

}