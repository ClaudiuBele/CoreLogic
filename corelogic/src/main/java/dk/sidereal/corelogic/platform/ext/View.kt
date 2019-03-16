package dk.sidereal.corelogic.platform.ext

import android.view.View
import android.view.ViewTreeObserver

inline fun View.afterMeasured(crossinline f: View.() -> Unit) {
    if (measuredWidth > 0 && measuredHeight > 0) {
        f()
        return
    }
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.compatRemoveOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

