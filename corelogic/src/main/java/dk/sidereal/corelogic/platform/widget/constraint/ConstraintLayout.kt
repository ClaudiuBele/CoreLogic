package dk.sidereal.corelogic.platform.widget.constraint

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

inline fun ConstraintLayout.applyConstraints(consumer: (ConstraintSet) -> Unit) {

    val constraintSet = ConstraintSet()
    constraintSet.clone(this)
    consumer.invoke(constraintSet)
    constraintSet.applyTo(this)
}

inline fun ConstraintLayout.applyViewConstraints(view: View, constraintApplier: (ConstraintApplier) -> Unit) {
    if (view.id == android.view.View.NO_ID) {
        view.id = android.view.View.generateViewId()
    }
    applyConstraints({ constraintSet: ConstraintSet ->
        constraintApplier.invoke(constraintSet.getConstraintApplier(view))
    })
}

inline fun ConstraintLayout.addViewAndApplyViewConstraints(view: View, index: Int = 0, constraintApplier: (ConstraintApplier) -> Unit) {
    if (view.id == android.view.View.NO_ID) {
        view.id = android.view.View.generateViewId()
    }
    addView(view, index)
    applyConstraints({ constraintSet: ConstraintSet ->
        constraintApplier.invoke(constraintSet.getConstraintApplier(view))
    })
}
