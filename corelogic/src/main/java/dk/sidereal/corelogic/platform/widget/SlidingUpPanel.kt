package dk.sidereal.corelogic.platform.widget

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams

import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.ViewDragHelper
import dk.sidereal.corelogic.R
import java.lang.reflect.Field

/** Subclass of [SlidingUpPanelLayout] which allows for touch events to pass through when
 * the view is collapsed.
 *
 * Unlike [SlidingUpPanelLayout] this view does not need a background view,
 * it will be automatically added at runtime, and it can be clicked through.
 *
 */
class SlidingUpPanel : SlidingUpPanelLayout {


    private var mDragViewResId : Int = -1

    private lateinit var slidingUpField: Field
    private lateinit var dragHelperField: Field

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs)
    }

    fun init(attrs: AttributeSet?) {
        slidingUpField = (javaClass.superclass!!).getDeclaredField("mIsSlidingUp").apply {
            isAccessible = true
        }
        dragHelperField = (javaClass.superclass!!).getDeclaredField("mDragHelper").apply {
            isAccessible = true
        }
        if(attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.SlidingUpPanel)
            ta?.let {
                mDragViewResId =  it.getResourceId(R.styleable.SlidingUpPanel_mini_view, -1)
            }
            ta.recycle()
        }
        val bgView = View(context).apply {
            isClickable = false
            isFocusable = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                focusable = NOT_FOCUSABLE
            }
//            updateLayoutParams<ViewGroup.LayoutParams> {
//                width = ViewGroup.LayoutParams.MATCH_PARENT
//                height = ViewGroup.LayoutParams.MATCH_PARENT
//            }
        }
        addView(bgView, 0)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (slidingUpField.getBoolean(this)) {
            if (panelState == PanelState.COLLAPSED && (ev.rawY < y + height - panelHeight || ev.rawY > y + height)) {
                return false
            }
            return super.onTouchEvent(ev)
        }

        try {
            val dragHelper = dragHelperField.get(this) as? ViewDragHelper
            dragHelper!!.processTouchEvent(ev)
        } catch (ex: Exception) {
            // Ignore the pointer out of range exception
            return false
        }

        if (panelState == PanelState.COLLAPSED) {

            // clicking on panel view
            if (ev.rawY >= y + height - panelHeight && ev.rawY < y + height) {
                return true
            }
            return false
        } else return panelState == PanelState.EXPANDED
    }
}
