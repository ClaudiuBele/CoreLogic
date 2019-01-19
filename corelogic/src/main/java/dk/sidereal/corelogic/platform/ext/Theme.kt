package dk.sidereal.corelogic.platform.ext

import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import dk.sidereal.corelogic.R

fun Resources.Theme.getColorInt(@AttrRes attrId: Int) = get(attrId, true).data

fun Resources.Theme.get(@AttrRes attrId: Int, resolveRefs: Boolean = false): TypedValue {
    val typedValue = android.util.TypedValue()
    resolveAttribute(attrId, typedValue, resolveRefs)
    return typedValue
}

fun Resources.Theme.getColorPrimaryInt() = getColorInt(R.attr.colorPrimary)
fun Resources.Theme.getColorPrimaryDarkInt() = getColorInt(R.attr.colorPrimaryDark)
fun Resources.Theme.getColorAccentInt() = getColorInt(R.attr.colorAccent)
fun Resources.Theme.getColorControlNormalInt() = getColorInt(R.attr.colorControlNormal)
fun Resources.Theme.getColorControlActivatedInt() = getColorInt(R.attr.colorControlActivated)
fun Resources.Theme.getColorControlHighlightInt() = getColorInt(R.attr.colorControlHighlight)