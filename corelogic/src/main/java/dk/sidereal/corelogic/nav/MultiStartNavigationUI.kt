package dk.sidereal.corelogic.nav

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination

/** Fix for supporting multiple start destinations
 *
 * Source: https://www.samclarke.com/android-navigation-component-multiple-start-destinations/
 *
 */
class MultiStartNavigationUI(private val startDestinations: List<Int>) {

    fun setupActionBarWithNavController(
        activity: AppCompatActivity, navController: NavController,
        drawerLayout: DrawerLayout?
    ) {

        navController.addOnNavigatedListener(
            ActionBarOnNavigatedListener(
                activity, startDestinations, drawerLayout
            )
        )
    }

    fun navigateUp(drawerLayout: DrawerLayout?, navController: NavController): Boolean {
        if (drawerLayout != null
            && startDestinations.contains(navController.currentDestination?.id)
            && drawerLayout.getDrawerLockMode(GravityCompat.START) != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {

            drawerLayout.openDrawer(GravityCompat.START)
            return true
        } else {
            return navController.navigateUp()
        }
    }

    fun onBackPressed(
        activity: AppCompatActivity,
        navController: NavController
    ): Boolean {
        if (startDestinations.contains(navController.currentDestination?.id)) {
            ActivityCompat.finishAfterTransition(activity)
            return true
        }

        return false
    }

    private class ActionBarOnNavigatedListener(
        private val mActivity: AppCompatActivity,
        private val startDestinations: List<Int>,
        private val mDrawerLayout: DrawerLayout?
    ) : NavController.OnNavigatedListener {
        private var mArrowDrawable: DrawerArrowDrawable? = null
        private var mAnimator: ValueAnimator? = null

        override fun onNavigated(controller: NavController, destination: NavDestination) {
            val actionBar = mActivity.supportActionBar

            val title = destination.label
            if (!title.isNullOrEmpty()) {
                actionBar?.title = title
            }

            val isStartDestination = startDestinations.contains(destination.id)
            actionBar?.setDisplayHomeAsUpEnabled(this.mDrawerLayout != null || !isStartDestination)
            setActionBarUpIndicator(mDrawerLayout != null && isStartDestination)
        }


        private fun setActionBarUpIndicator(showAsDrawerIndicator: Boolean) {
            val delegate = mActivity.drawerToggleDelegate
            var animate = true
            if (mArrowDrawable == null) {
                mArrowDrawable = DrawerArrowDrawable(delegate!!.actionBarThemedContext)
                delegate.setActionBarUpIndicator(mArrowDrawable, 0)
                animate = false
            }

            mArrowDrawable?.let {
                val endValue = if (showAsDrawerIndicator) 0.0f else 1.0f

                if (animate) {
                    val startValue = it.progress
                    mAnimator?.cancel()

                    @SuppressLint("ObjectAnimatorBinding")
                    mAnimator = ObjectAnimator.ofFloat(it, "progress", startValue, endValue)
                    mAnimator?.start()
                } else {
                    it.progress = endValue
                }
            }

        }
    }
}