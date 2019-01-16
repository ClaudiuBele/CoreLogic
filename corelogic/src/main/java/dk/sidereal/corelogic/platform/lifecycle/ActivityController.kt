package dk.sidereal.corelogic.platform.lifecycle

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.CallSuper
import androidx.lifecycle.LifecycleObserver
import dk.sidereal.corelogic.kotlin.ext.simpleTagName

/** Activity controller. Contains a reference to a BaseActivity in order to delegate activity callbacks ([LifecycleObserver]
 * is not enough) in isolated units of logic. Must be created in [BaseActivity.onCreate]
 *
 */
abstract class ActivityController(protected val activity: BaseActivity) {

    protected val TAG by lazy { javaClass.simpleTagName() }

    companion object {
        val INNER_TAG by lazy { ActivityController::class.simpleTagName() }
    }


    val applicationControllers: List<ApplicationController>
        get() = activity.baseApplication.controllers

    val context = activity


    /** Called after [BaseActivity.onAttachFragment] inside the override
     */
    open fun onAttachFragment(baseFragment: BaseFragment?) {

    }

    /** Called in [BaseActivity.onCreate] after
     * [BaseActivity.onCreateControllers]
     */
    open fun onCreate(savedInstanceState: Bundle?) {}

    /** Called in [BaseActivity.onDestroy]
     *
     */
    open fun onDestroy() {}

    /** Called in [BaseActivity.onCreate]. Return true if you are setting the content view in this controller.
     *
     */
    open fun onCreateView(baseActivity: BaseActivity): Boolean = false

    /** Called in [BaseActivity.onCreate] after it calls [onCreateView] on all controllers
     */
    @CallSuper
    open fun onViewCreated(baseActivity: BaseActivity) {
    }

    /** Called in [BaseActivity.onSupportNavigateUp]
     */
    open fun onNavigateUp(): Boolean = false

    /** Called in [BaseActivity.onOptionsItemSelected]
     */
    open fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    /** Called in [BaseActivity.onBackPressed]
     */
    open fun onBackPressed(): Boolean = false

    /** Called in [BaseActivity.onSaveInstanceState]
     */
    @CallSuper
    open fun onSaveInstanceState(outState: Bundle) {
    }
}