package dk.sidereal.corelogic.platform.ext

import android.view.View
import android.view.ViewGroup


inline fun ViewGroup.onChildView(crossinline onAdded: (parent: View?, child: View?) -> Unit,
                                    crossinline onRemoved: (parent: View?, child: View?) -> Unit) {
    setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener{
        override fun onChildViewRemoved(parent: View?, child: View?) {
            onChildViewRemoved(parent, child)
        }

        override fun onChildViewAdded(parent: View?, child: View?) {
            onChildViewAdded(parent, child)
        }
    })
}