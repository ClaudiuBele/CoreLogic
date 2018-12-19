package dk.sidereal.corelogic.platform.lifecycle

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.CallSuper

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

    /** Called in [BaseActivity.onCreate]. Return true if you are setting the content view in this controller.
     *
     */
    open fun onCreateView(baseActivity: BaseActivity) : Boolean { return false }

    /** Called in [BaseActivity.onCreate] after it calls [onCreateView] on all controllers
     *
     */
    @CallSuper
    open fun onViewCreated(baseActivity: BaseActivity) {}

    /** Called in [BaseActivity.onSupportNavigateUp]
     *
     */
    open fun onNavigateUp() : Boolean = false

    /** Called in [BaseActivity.onOptionsItemSelected]
     *
     */
    open fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    /** Called in [BaseActivity.onBackPressed]
     *
     */
    open fun onBackPressed(): Boolean = false
}