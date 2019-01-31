package dk.sidereal.corelogic.platform.widget

import android.view.View
import dk.sidereal.corelogic.platform.ext.afterMeasured

object Views {

    fun afterViewsMeasured(views: List<View>, onMeasured: (List<View>) -> Unit) {
        if (views.isEmpty()) {
            onMeasured(views)
            return
        }
        views.first().afterMeasured {
            val newList = if (views.size == 1) {
                listOf()
            } else {
                views.subList(1, views.size)
            }
            afterViewsMeasured(newList, onMeasured)
        }
    }

}