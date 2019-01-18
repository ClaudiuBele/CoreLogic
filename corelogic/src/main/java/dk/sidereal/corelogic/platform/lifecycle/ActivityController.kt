package dk.sidereal.corelogic.platform.lifecycle

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.CallSuper
import androidx.lifecycle.LifecycleObserver
import dk.sidereal.corelogic.kotlin.ext.simpleTagName

/** Activity controller. Contains a reference to a CoreActivity in order to delegate activity callbacks ([LifecycleObserver]
 * is not enough) in isolated units of logic. Must be created in [CoreActivity.onCreate]
 *
 */
abstract class ActivityController(protected val activity: CoreActivity) {

    protected val TAG by lazy { javaClass.simpleTagName() }

    companion object {
        val INNER_TAG by lazy { ActivityController::class.simpleTagName() }
    }

    protected val context: Context = activity

    /** Called after [CoreActivity.onAttachFragment] inside the override
     */
    open fun onAttachFragment(coreFragment: CoreFragment?) {

    }

    /** Called in [CoreActivity.onCreate] after
     * [CoreActivity.onCreateControllers]
     */
    open fun onCreate(savedInstanceState: Bundle?) {}


    /** Called in [CoreActivity.onCreate]. Return true if you are setting the content view in this controller.
     *
     */
    open fun onCreateView(coreActivity: CoreActivity): Boolean = false

    /** Called in [CoreActivity.onDestroy]
     *
     */
    open fun onDestroy() {}


    /** Called in [CoreActivity.onCreate] after it calls [onCreateView] on all controllers
     */
    @CallSuper
    open fun onViewCreated(coreActivity: CoreActivity) {
    }

    /** Called in [CoreActivity.onSupportNavigateUp]
     */
    open fun onNavigateUp(): Boolean = false

    /** Called in [CoreActivity.onOptionsItemSelected]
     */
    open fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    /** Called in [CoreActivity.onBackPressed]
     */
    open fun onBackPressed(): Boolean = false

    /** Called in [CoreActivity.onSaveInstanceState]
     */
    @CallSuper
    open fun onSaveInstanceState(outState: Bundle) {
    }
}