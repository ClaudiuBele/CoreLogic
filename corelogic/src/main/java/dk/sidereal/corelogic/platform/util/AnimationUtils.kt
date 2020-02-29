package dk.sidereal.corelogic.platform.util

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator

object AnimationUtils {

    val LINEAR_INTERPOLATOR: Interpolator = LinearInterpolator()
    val FAST_OUT_SLOW_IN_INTERPOLATOR: Interpolator = FastOutSlowInInterpolator()
    val FAST_OUT_LINEAR_IN_INTERPOLATOR: Interpolator = FastOutLinearInInterpolator()
    val LINEAR_OUT_SLOW_IN_INTERPOLATOR: Interpolator = LinearOutSlowInInterpolator()
    val DECELERATE_INTERPOLATOR: Interpolator = DecelerateInterpolator()

    fun animateAlpha(
        view: View,
        from: Float,
        to: Float,
        duration: Long = 200L,
        onDone: (() -> Unit)? = null
    ) {
        ValueAnimator.ofFloat(from, to).apply {
            var prevAlpha = view.alpha
            addUpdateListener {
                if (view.alpha == prevAlpha) {
                    view.alpha = it.animatedValue as Float
                    prevAlpha = view.alpha
                }
            }
            addListener(onEnd = {
                onDone?.invoke()
            })
            start()
        }
    }

    /**
     * Linear interpolation between `startValue` and `endValue` by `fraction`.
     */
    fun lerp(startValue: Float, endValue: Float, fraction: Float): Float {
        return startValue + fraction * (endValue - startValue)
    }

    fun lerp(startValue: Int, endValue: Int, fraction: Float): Int {
        return startValue + Math.round(fraction * (endValue - startValue))
    }
}