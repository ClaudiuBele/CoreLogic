package dk.sidereal.corelogic.platform.util

import android.content.Context
import android.content.res.Configuration

object ContextUtils {

    fun getNightMode(context: Context): Boolean? {
        val nightModeFlags = context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK

        val nightMode = when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> null
            else -> null
        }
        return nightMode
    }

    fun Context.onNightMode(nightModeOn: (Boolean?) -> Unit) {
        getNightMode(this).apply(nightModeOn)
    }
}
