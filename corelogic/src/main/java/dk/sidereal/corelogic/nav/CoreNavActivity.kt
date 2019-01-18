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
 * If you are extending this, you can implement a custom layout by overriding [setNavContentView]
 *
 * Following https://developer.android.com/topic/libraries/architecture/navigation/navigation-implementing
 */
abstract class CoreNavActivity : CoreActivity() {

    companion object {
        val DEBUG_TAG = "NAV"
        val NAV_HOST_ROOT_ID = R.id.nav_host_fragment_root
    }

    lateinit var navController: NavController
    lateinit var navHostRoot: ViewGroup

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
    override fun onCreateControllers(controllers: MutableList<ActivityController>) {
        super.onCreateControllers(controllers)
        controllers.add(onCreateNavigationController())
    }

    open fun onCreateNavigationController(): CoreNavActivityController {
        return object : CoreNavActivityController(this@CoreNavActivity) {
            override fun getNavHostFragment(): CoreNavHostFragment {
                return this@CoreNavActivity.getNavHostFragment()
            }
        }
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

    /** Abstract function that must be implemented. Create your own fragment using a nav resource like this
     *  [CoreNavHostFragment.create(R.navigation.nav_main)]
     *
     */
    abstract fun getNavHostFragment(): CoreNavHostFragment


}