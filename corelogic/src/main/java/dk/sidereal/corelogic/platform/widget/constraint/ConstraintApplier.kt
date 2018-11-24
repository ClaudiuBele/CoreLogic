package dk.sidereal.corelogic.platform.widget.constraint

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet

class ConstraintApplier(val view: View, val constraintSet: ConstraintSet) {

    companion object {
        // spread is default
        val supportedDefaultWidths: Array<Int> = arrayOf(ConstraintSet.MATCH_CONSTRAINT_WRAP, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
    }

    fun centerInParentHorizontally(): ConstraintApplier {
        constraintSet.centerInParentHorizontal(view)
        return this
    }

    fun centerInParentVertically(): ConstraintApplier {
        constraintSet.centerInParentVertical(view)
        return this
    }

    fun setVisibility(visibility: Int): ConstraintApplier {
        constraintSet.setVisibility(view.id, visibility)
        return this
    }

    fun anchorToParentSide(side: Int): ConstraintApplier {
        constraintSet.anchorToParentSide(view, side)
        return this
    }

    fun connect(side: Int, targetViewId: Int, targetSide: Int): ConstraintApplier {
        constraintSet.connect(view.id, side, targetViewId, targetSide)
        return this
    }

    fun disconnect(side: Int): ConstraintApplier {
        constraintSet.clear(view.id, side)
        return this
    }

    fun setMargin(side: Int, value: Int): ConstraintApplier {
        constraintSet.setMargin(view.id, side, value)
        return this
    }

    fun setAlpha(alpha: Float): ConstraintApplier {
        constraintSet.setAlpha(view.id, alpha)
        return this
    }

    // looks like "percent" not handled, so only use [ConstraintSet.MATCH_CONSTRAINT_SPREAD] and
    // [MATCH_CONSTRAINT_WRAP]
    fun constraintWidthDefault(defaultWidth: Int): ConstraintApplier {
        if (supportedDefaultWidths.contains(defaultWidth)) {
            constraintSet.constrainDefaultWidth(view.id, defaultWidth)
        }
        return this
    }

}