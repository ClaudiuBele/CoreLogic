package dk.sidereal.corelogic.platform.ext

import androidx.recyclerview.widget.RecyclerView

/** Returns all viewholders
 *
 */
inline fun <reified T: RecyclerView.ViewHolder>RecyclerView.getViewholders(): List<T> {
    return (0 until childCount)
        .map { getChildAt(it) }
        .map { getChildViewHolder(it) }
        .filterIsInstance<T>()
}