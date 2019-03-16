package dk.sidereal.corelogic.nav

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.navigation.NavController
import dk.sidereal.corelogic.R
import dk.sidereal.corelogic.platform.lifecycle.ActivityController
import dk.sidereal.corelogic.platform.lifecycle.CoreActivity

/** CoreActivity that has a NavHostFragment. Must implement [getNavHostFragment].
 *
 * Following https://developer.android.com/topic/libraries/architecture/navigation/navigation-implementing
 */
abstract class CoreNavActivity
    : CoreActivity(),
    CoreNavActivityController.NavFragmentCreator {

    companion object {
        val DEBUG_TAG = "NAV"
    }

    /** Don't call [setContentView] here. If you want a custom layout override
     * [setNavContentView]
     */
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(DEBUG_TAG, "CoreNavActivity: onCreate")
    }

    /** Implementations of [NavActivity] must override [onCreateNavigationController] if they want to provide a different controller
     *
     */
    @CallSuper
    override fun onCreateControllers(outControllers: MutableList<ActivityController>) {
        super.onCreateControllers(outControllers)
        outControllers.add(onCreateNavigationController())
    }

    /** If you don't want to go the path of using a [CoreNavHostFragment], your
     * [Navigation.findNavController(this, R.id.nav_host_fragment)] [NavController] will start being not null here, where
     * [R.id.nav_host_fragment_root] points to a [FrameLayout] which we are passing as the id of our
     * [androidx.fragment.app.FragmentTransaction.replace] call.
     *
     */
    override fun onStart() {
        super.onStart()
        Log.d(DEBUG_TAG, "CoreNavActivity: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(DEBUG_TAG, "CoreNavActivity: onResume")
    }

    /** Called in [CoreNavActivity.onCreateControllers]. This type of activity's contract is needing a [CoreNavActivityController],
     * which achieves navigation.
     */
    abstract fun onCreateNavigationController(): CoreNavActivityController

    /** In the activity layout, The view above [CoreNavHostFragment] view, works outside the navigation flow.
     * Perfect for [dk.sidereal.corelogic.platform.widget.SlidingUpPanel]
     */
    val overlayContainer: ViewGroup
        get() = getController(CoreNavActivityController::class.java)!!.overlayContainer

}