package dk.sidereal.corelogic.platform.widget.constraint

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet

fun ConstraintSet.centerInParentHorizontal(view: View) {
    connect(view.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
    connect(view.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
}

fun ConstraintSet.centerInParentVertical(view: View) {
    connect(view.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
    connect(view.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
}

fun ConstraintSet.anchorToParentSide(view: View, side: Int) {
    connect(view.id, side, ConstraintSet.PARENT_ID, side)
}

fun ConstraintSet.getConstraintApplier(view: View) = ConstraintApplier(view, this)