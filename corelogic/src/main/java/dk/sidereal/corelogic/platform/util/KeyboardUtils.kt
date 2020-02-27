package dk.sidereal.corelogic.platform.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import dk.sidereal.corelogic.platform.lifecycle.CoreFragment

fun showKeyboard(view: View) {
    if (view.requestFocus()) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.showSoftInput(view,
            InputMethodManager.SHOW_IMPLICIT
        )
    }
}

fun hideKeyboard(baseFragment: CoreFragment) {
    // Check if no view has focus:
    baseFragment.activity?.currentFocus?.let {
        hideKeyboard(it)
    }
}

fun hideKeyboard(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    // Find the currently focused view, so we can grab the correct window token from it.
    var view = activity.currentFocus
    // If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun setupUiForKeyboardDismiss(activity: Activity, rootView: View) {
    // Set up touch listener for non-text box views to hide keyboard.
    if (rootView !is EditText) {
        rootView.setOnTouchListener { _, _ ->
            hideKeyboard(activity)
            false
        }
    }

    // If a layout container, iterate over children and seed recursion.
    if (rootView is ViewGroup) {
        for (i in 0 until rootView.childCount) {
            val innerView = rootView.getChildAt(i)
            setupUiForKeyboardDismiss(activity, innerView)
        }
    }
}
