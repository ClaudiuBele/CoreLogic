package dk.sidereal.corelogic.platform.lifecycle

import android.os.Bundle

/** Activity controller. Contains a reference to a BaseActivity in order to delegate logic related to it
 * away from the activity. Should be created in [BaseActivity.onCreate], and [dispose] is called from [BaseActivity.onDestroy]
 *
 */
abstract class ActivityController(protected val activity: BaseActivity)  {

    val applicationControllers: List<ApplicationController>
    get() = activity.baseApplication.controllers

    val context = activity

    /** Called in [BaseActivity.onDestroy]
     *
     */
    open fun dispose() {}

    /** Called after [BaseActivity.onAttachFragment] inside the override
     */
    open fun onAttachFragment(baseFragment: BaseFragment?) {

    }

    /** Called in [BaseActivity.onCreate] after
     * [BaseActivity.onSetupControllers]
     */
    open fun onCreate(savedInstanceState: Bundle?) {}
}