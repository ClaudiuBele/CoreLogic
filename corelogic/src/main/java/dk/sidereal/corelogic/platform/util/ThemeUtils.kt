package dk.sidereal.corelogic.platform.util

import android.content.Context
import android.graphics.Color

object ThemeUtils {

    fun getThemeAttrColor(context: Context?, attr: Int): Int {
        val TEMP_ARRAY = IntArray(1)
        TEMP_ARRAY[0] = attr
        val a = context?.theme?.obtainStyledAttributes(TEMP_ARRAY)
        return try {
            a?.getColor(0, Color.MAGENTA) ?: Color.MAGENTA
        } finally {
            a?.recycle()
        }
    }

    fun getStatusBarSize(context: Context): Int {
        var result = 0
        val resourceId: Int =
            context.getResources()?.getIdentifier("status_bar_height", "dimen", "android") ?: 0
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId)
        }
        return result
    }
}