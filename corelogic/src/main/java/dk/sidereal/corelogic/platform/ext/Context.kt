package dk.sidereal.corelogic.platform.ext

import android.content.Context
import dk.sidereal.corelogic.R

fun Context.getAppName(): String {
    return getString(R.string.app_name)
}