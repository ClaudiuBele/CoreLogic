package dk.sidereal.corelogic.platform.ext

import android.os.Build
import android.view.ViewTreeObserver

fun ViewTreeObserver.compatRemoveOnGlobalLayoutListener(onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        removeOnGlobalLayoutListener(onGlobalLayoutListener)
    } else {
        removeGlobalOnLayoutListener(onGlobalLayoutListener)
    }
}
