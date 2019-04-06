package dk.sidereal.corelogic.platform

/** Interface for generalizing controllers for different parts of the android framework.
 *
 * Currently, the following platform classes are supported.
 *
 * - [android.app.Application] through [dk.sidereal.corelogic.platform.lifecycle.ApplicationController]
 * - [android.app.Activity] through [dk.sidereal.corelogic.platform.lifecycle.ActivityController]
 * - [androidx.fragment.app.Fragment] through [dk.sidereal.corelogic.platform.lifecycle.FragmentController]
 *
 */
interface AndroidModelController<T> {

    val model: T

}