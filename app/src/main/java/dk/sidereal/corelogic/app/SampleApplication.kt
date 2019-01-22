package dk.sidereal.corelogic.app

import android.app.Application

/** Used to show compatibility between non-[dk.sidereal.corelogic.platform.lifecycle.CoreApplication] and
 * [dk.sidereal.corelogic.platform.lifecycle.CoreActivity] & [dk.sidereal.corelogic.platform.lifecycle.CoreFragment] logic.
 *
 * Ommiting an application class is also valid, you have a single activity, but there is some value in doing things here like
 * adding components and initialising 3rd party sdks in [onCreate].
 *
 * @see [dk.sidereal.corelogic.di.lifecycle.DiApplication]
 * @see [dk.sidereal.corelogic.platform.lifecycle.CoreApplication]
 *
 */
class SampleApplication : Application() {

}