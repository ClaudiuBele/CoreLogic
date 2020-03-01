/*
 * Copyright (C) 2015 The Android Open Source Project
 * Modified 2015 by Johan v. Forstner (modifications are marked with comments)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.opacapp.multilinecollapsingtoolbar

import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.*
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.animation.Interpolator
import androidx.annotation.ColorInt
import androidx.core.math.MathUtils
import androidx.core.text.TextDirectionHeuristicsCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import dk.sidereal.corelogic.platform.util.AnimationUtils

// BEGIN MODIFICATION: Added imports
// END MODIFICATION
internal class CollapsingTextHelper(private val mView: View) {
    companion object {
        // Pre-JB-MR2 doesn't support HW accelerated canvas scaled text so we will workaround it
// by using our own texture
        private val USE_SCALING_TEXTURE: Boolean = Build.VERSION.SDK_INT < 18
        private val DEBUG_DRAW: Boolean = false
        private var DEBUG_DRAW_PAINT: Paint? = null
        /**
         * Returns true if `value` is 'close' to it's closest decimal value. Close is currently
         * defined as it's difference being < 0.001.
         */
        private fun isClose(value: Float, targetValue: Float): Boolean {
            return Math.abs(value - targetValue) < 0.001f
        }

        /**
         * Blend `color1` and `color2` using the given ratio.
         *
         * @param ratio of which to blend. 0.0 will return `color1`, 0.5 will give an even blend,
         * 1.0 will return `color2`.
         */
        private fun blendColors(color1: Int, color2: Int, ratio: Float): Int {
            val inverseRatio: Float = 1f - ratio
            val a: Float = (Color.alpha(color1) * inverseRatio) + (Color.alpha(color2) * ratio)
            val r: Float = (Color.red(color1) * inverseRatio) + (Color.red(color2) * ratio)
            val g: Float = (Color.green(color1) * inverseRatio) + (Color.green(color2) * ratio)
            val b: Float = (Color.blue(color1) * inverseRatio) + (Color.blue(color2) * ratio)
            return Color.argb(a.toInt(), r.toInt(), g.toInt(), b.toInt())
        }

        private fun lerp(
            startValue: Float, endValue: Float, fraction: Float,
            interpolator: Interpolator?
        ): Float {
            var fraction: Float = fraction
            if (interpolator != null) {
                fraction = interpolator.getInterpolation(fraction)
            }
            return AnimationUtils.lerp(startValue, endValue, fraction)
        }

        private fun rectEquals(r: Rect, left: Int, top: Int, right: Int, bottom: Int): Boolean {
            return !((r.left != left) || (r.top != top) || (r.right != right) || (r.bottom != bottom))
        }

        init {
            DEBUG_DRAW_PAINT =
                if (DEBUG_DRAW) Paint() else null
            if (DEBUG_DRAW_PAINT != null) {
                DEBUG_DRAW_PAINT!!.setAntiAlias(true)
                DEBUG_DRAW_PAINT!!.setColor(Color.MAGENTA)
            }
        }
    }

    private var mDrawTitle: Boolean = false
    private var mExpandedFraction: Float = 0f
    private val mExpandedBounds: Rect
    private val mCollapsedBounds: Rect
    private val mCurrentBounds: RectF
    private var mExpandedTextGravity: Int = Gravity.CENTER_VERTICAL
    private var mCollapsedTextGravity: Int = Gravity.CENTER_VERTICAL
    private var mExpandedTextSize: Float = 15f
    private var mCollapsedTextSize: Float = 15f
    var expandedTextColor: ColorStateList? = null
        private set
    var collapsedTextColor: ColorStateList? = null
        private set
    private var mExpandedDrawY: Float = 0f
    private var mCollapsedDrawY: Float = 0f
    private var mExpandedDrawX: Float = 0f
    private var mCollapsedDrawX: Float = 0f
    private var mCurrentDrawX: Float = 0f
    private var mCurrentDrawY: Float = 0f
    private var mCollapsedTypeface: Typeface? = null
    private var mExpandedTypeface: Typeface? = null
    private var mCurrentTypeface: Typeface? = null
    private var mText: CharSequence? = null
    private var mTextToDraw: CharSequence? = null
    private var mIsRtl: Boolean = false
    private var mUseTexture: Boolean = false
    private var mExpandedTitleTexture: Bitmap? = null
    private var mTexturePaint: Paint? = null
    // MODIFICATION: Removed now unused fields mTextureAscent and mTextureDescent
    private var mScale: Float = 0f
    private var mCurrentTextSize: Float = 0f
    private var mState: IntArray? = null
    private var mBoundsChanged: Boolean = false
    private val mTextPaint: TextPaint
    private var mPositionInterpolator: Interpolator? = null
    private var mTextSizeInterpolator: Interpolator? = null
    private var mCollapsedShadowRadius: Float = 0f
    private var mCollapsedShadowDx: Float = 0f
    private var mCollapsedShadowDy: Float = 0f
    private var mCollapsedShadowColor: Int = 0
    private var mExpandedShadowRadius: Float = 0f
    private var mExpandedShadowDx: Float = 0f
    private var mExpandedShadowDy: Float = 0f
    private var mExpandedShadowColor: Int = 0
    // BEGIN MODIFICATION: Added fields
    private var mTextToDrawCollapsed: CharSequence? = null
    private var mCollapsedTitleTexture: Bitmap? = null
    private var mCrossSectionTitleTexture: Bitmap? = null
    private var mTextLayout: StaticLayout? = null
    private var mCollapsedTextBlend: Float = 0f
    private var mExpandedTextBlend: Float = 0f
    private var mExpandedFirstLineDrawX: Float = 0f
    private var maxLines: Int = 3
    private var lineSpacingExtra: Float = 0f
    private var lineSpacingMultiplier: Float = 1f
    fun setTextSizeInterpolator(interpolator: Interpolator?) {
        mTextSizeInterpolator = interpolator
        recalculate()
    }

    fun setPositionInterpolator(interpolator: Interpolator?) {
        mPositionInterpolator = interpolator
        recalculate()
    }

    fun setCollapsedTextColor(textColor: ColorStateList) {
        if (collapsedTextColor !== textColor) {
            collapsedTextColor = textColor
            recalculate()
        }
    }

    fun setExpandedTextColor(textColor: ColorStateList) {
        if (expandedTextColor !== textColor) {
            expandedTextColor = textColor
            recalculate()
        }
    }

    fun setExpandedBounds(left: Int, top: Int, right: Int, bottom: Int) {
        if (!rectEquals(mExpandedBounds, left, top, right, bottom)) {
            mExpandedBounds.set(left, top, right, bottom)
            mBoundsChanged = true
            onBoundsChanged()
        }
    }

    fun setCollapsedBounds(left: Int, top: Int, right: Int, bottom: Int) {
        if (!rectEquals(mCollapsedBounds, left, top, right, bottom)) {
            mCollapsedBounds.set(left, top, right, bottom)
            mBoundsChanged = true
            onBoundsChanged()
        }
    }

    fun onBoundsChanged() {
        mDrawTitle = (mCollapsedBounds.width() > 0) && (mCollapsedBounds.height() > 0
                ) && (mExpandedBounds.width() > 0) && (mExpandedBounds.height() > 0)
    }

    var expandedTextGravity: Int
        get() = mExpandedTextGravity
        set(gravity) {
            if (mExpandedTextGravity != gravity) {
                mExpandedTextGravity = gravity
                recalculate()
            }
        }

    var collapsedTextGravity: Int
        get() = mCollapsedTextGravity
        set(gravity) {
            if (mCollapsedTextGravity != gravity) {
                mCollapsedTextGravity = gravity
                recalculate()
            }
        }

    fun setCollapsedTextAppearance(resId: Int) {
        val a: TypedArray = mView.context.obtainStyledAttributes(
            resId,
            R.styleable.TextAppearance
        )
        if (a.hasValue(R.styleable.TextAppearance_android_textColor)) {
            collapsedTextColor = a.getColorStateList(
                R.styleable.TextAppearance_android_textColor
            )
        }
        if (a.hasValue(R.styleable.TextAppearance_android_textSize)) {
            mCollapsedTextSize = a.getDimensionPixelSize(
                R.styleable.TextAppearance_android_textSize,
                mCollapsedTextSize.toInt()
            ).toFloat()
        }
        mCollapsedShadowColor = a.getInt(
            R.styleable.TextAppearance_android_shadowColor, 0
        )
        mCollapsedShadowDx = a.getFloat(
            R.styleable.TextAppearance_android_shadowDx, 0f
        )
        mCollapsedShadowDy = a.getFloat(
            R.styleable.TextAppearance_android_shadowDy, 0f
        )
        mCollapsedShadowRadius = a.getFloat(
            R.styleable.TextAppearance_android_shadowRadius, 0f
        )
        a.recycle()
        if (Build.VERSION.SDK_INT >= 16) {
            mCollapsedTypeface = readFontFamilyTypeface(resId)
        }
        recalculate()
    }

    fun setExpandedTextAppearance(resId: Int) {
        val a: TypedArray = mView.context.obtainStyledAttributes(
            resId,
            R.styleable.TextAppearance
        )
        if (a.hasValue(R.styleable.TextAppearance_android_textColor)) {
            expandedTextColor = a.getColorStateList(
                R.styleable.TextAppearance_android_textColor
            )
        }
        if (a.hasValue(R.styleable.TextAppearance_android_textSize)) {
            mExpandedTextSize = a.getDimensionPixelSize(
                R.styleable.TextAppearance_android_textSize,
                mExpandedTextSize.toInt()
            ).toFloat()
        }
        mExpandedShadowColor = a.getInt(
            R.styleable.TextAppearance_android_shadowColor, 0
        )
        mExpandedShadowDx = a.getFloat(
            R.styleable.TextAppearance_android_shadowDx, 0f
        )
        mExpandedShadowDy = a.getFloat(
            R.styleable.TextAppearance_android_shadowDy, 0f
        )
        mExpandedShadowRadius = a.getFloat(
            R.styleable.TextAppearance_android_shadowRadius, 0f
        )
        a.recycle()
        if (Build.VERSION.SDK_INT >= 16) {
            mExpandedTypeface = readFontFamilyTypeface(resId)
        }
        recalculate()
    }

    // BEGIN MODIFICATION: getter and setter method for number of max lines
    fun setMaxLines(maxLines: Int) {
        if (maxLines != this.maxLines) {
            this.maxLines = maxLines
            clearTexture()
            recalculate()
        }
    }

    fun getMaxLines(): Int {
        return maxLines
    }

    // END MODIFICATION
// BEGIN MODIFICATION: getter and setter methods for line spacing
    fun setLineSpacingExtra(lineSpacingExtra: Float) {
        if (lineSpacingExtra != this.lineSpacingExtra) {
            this.lineSpacingExtra = lineSpacingExtra
            clearTexture()
            recalculate()
        }
    }

    fun getLineSpacingExtra(): Float {
        return lineSpacingExtra
    }

    fun setLineSpacingMultiplier(lineSpacingMultiplier: Float) {
        if (lineSpacingMultiplier != this.lineSpacingMultiplier) {
            this.lineSpacingMultiplier = lineSpacingMultiplier
            clearTexture()
            recalculate()
        }
    }

    fun getLineSpacingMultiplier(): Float {
        return lineSpacingMultiplier
    }

    // END MODIFICATION
    private fun readFontFamilyTypeface(resId: Int): Typeface? {
        val a: TypedArray = mView.context.obtainStyledAttributes(resId, intArrayOf(R.attr.fontFamily))
        try {
            val family: String? = a.getString(0)
            if (family != null) {
                return Typeface.create(family, Typeface.NORMAL)
            }
        } finally {
            a.recycle()
        }
        return null
    }

    fun setTypefaces(typeface: Typeface?) {
        mExpandedTypeface = typeface
        mCollapsedTypeface = mExpandedTypeface
        recalculate()
    }

    var collapsedTypeface: Typeface?
        get() = if (mCollapsedTypeface != null) mCollapsedTypeface else Typeface.DEFAULT
        set(typeface) {
            if (areTypefacesDifferent(mCollapsedTypeface, typeface)) {
                mCollapsedTypeface = typeface
                recalculate()
            }
        }

    var expandedTypeface: Typeface?
        get() = if (mExpandedTypeface != null) mExpandedTypeface else Typeface.DEFAULT
        set(typeface) {
            if (areTypefacesDifferent(mExpandedTypeface, typeface)) {
                mExpandedTypeface = typeface
                recalculate()
            }
        }

    fun setState(state: IntArray?): Boolean {
        mState = state
        if (isStateful) {
            recalculate()
            return true
        }
        return false
    }

    val isStateful: Boolean
        get() = (collapsedTextColor != null && collapsedTextColor!!.isStateful) ||
                (expandedTextColor != null && expandedTextColor!!.isStateful)

    /**
     * Set the value indicating the current scroll value. This decides how much of the
     * background will be displayed, as well as the title metrics/positioning.
     *
     * A value of `0.0` indicates that the layout is fully expanded.
     * A value of `1.0` indicates that the layout is fully collapsed.
     */
    var expansionFraction: Float
        get() {
            return mExpandedFraction
        }
        set(fraction) {
            var fraction: Float = fraction
            fraction = MathUtils.clamp(fraction, 0f, 1f)
            if (fraction != mExpandedFraction) {
                mExpandedFraction = fraction
                calculateCurrentOffsets()
            }
        }

    var collapsedTextSize: Float
        get() {
            return mCollapsedTextSize
        }
        set(textSize) {
            if (mCollapsedTextSize != textSize) {
                mCollapsedTextSize = textSize
                recalculate()
            }
        }

    var expandedTextSize: Float
        get() {
            return mExpandedTextSize
        }
        set(textSize) {
            if (mExpandedTextSize != textSize) {
                mExpandedTextSize = textSize
                recalculate()
            }
        }

    private fun calculateCurrentOffsets() {
        calculateOffsets(mExpandedFraction)
    }

    private fun calculateOffsets(fraction: Float) {
        interpolateBounds(fraction)
        mCurrentDrawX = lerp(
            mExpandedDrawX, mCollapsedDrawX, fraction,
            mPositionInterpolator
        )
        mCurrentDrawY = lerp(
            mExpandedDrawY, mCollapsedDrawY, fraction,
            mPositionInterpolator
        )
        setInterpolatedTextSize(
            lerp(
                mExpandedTextSize, mCollapsedTextSize,
                fraction, mTextSizeInterpolator
            )
        )
        // BEGIN MODIFICATION: set text blending
        setCollapsedTextBlend(
            1 - lerp(
                0f,
                1f,
                1 - fraction,
                AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR
            )
        )
        setExpandedTextBlend(
            lerp(
                1f,
                0f,
                fraction,
                AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR
            )
        )
        // END MODIFICATION
        if (collapsedTextColor !== expandedTextColor) { // If the collapsed and expanded text colors are different, blend them based on the
// fraction
            mTextPaint.color = blendColors(
                currentExpandedTextColor, currentCollapsedTextColor, fraction
            )
        } else {
            mTextPaint.color = currentCollapsedTextColor
        }
        mTextPaint.setShadowLayer(
            lerp(
                mExpandedShadowRadius,
                mCollapsedShadowRadius,
                fraction,
                null
            ),
            lerp(
                mExpandedShadowDx,
                mCollapsedShadowDx,
                fraction,
                null
            ),
            lerp(
                mExpandedShadowDy,
                mCollapsedShadowDy,
                fraction,
                null
            ),
            blendColors(
                mExpandedShadowColor,
                mCollapsedShadowColor,
                fraction
            )
        )
        ViewCompat.postInvalidateOnAnimation(mView)
    }

    @get:ColorInt
    private val currentExpandedTextColor: Int
        private get() {
            if (mState != null) {
                return expandedTextColor!!.getColorForState(mState, 0)
            } else {
                return expandedTextColor!!.defaultColor
            }
        }

    @get:ColorInt
    private val currentCollapsedTextColor: Int
        private get() {
            if (mState != null) {
                return collapsedTextColor!!.getColorForState(mState, 0)
            } else {
                return collapsedTextColor!!.defaultColor
            }
        }

    private fun calculateBaseOffsets() {
        val currentTextSize: Float = mCurrentTextSize
        // We then calculate the collapsed text size, using the same logic
        calculateUsingTextSize(mCollapsedTextSize)
        // BEGIN MODIFICATION: set mTextToDrawCollapsed and calculate width using it
        mTextToDrawCollapsed = mTextToDraw
        var width: Float =
            if (mTextToDrawCollapsed != null) mTextPaint.measureText(mTextToDrawCollapsed, 0, mTextToDrawCollapsed!!.length) else 0F
        // END MODIFICATION
        val collapsedAbsGravity: Int = GravityCompat.getAbsoluteGravity(
            mCollapsedTextGravity,
            if (mIsRtl) ViewCompat.LAYOUT_DIRECTION_RTL else ViewCompat.LAYOUT_DIRECTION_LTR
        )
        // BEGIN MODIFICATION: calculate height and Y position using mTextLayout
        var textHeight: Float = if (mTextLayout != null) mTextLayout!!.height.toFloat() else 0.toFloat()
        when (collapsedAbsGravity and Gravity.VERTICAL_GRAVITY_MASK) {
            Gravity.BOTTOM -> mCollapsedDrawY = mCollapsedBounds.bottom - textHeight
            Gravity.TOP -> mCollapsedDrawY = mCollapsedBounds.top.toFloat()
            Gravity.CENTER_VERTICAL -> {
                val textOffset: Float = (textHeight / 2)
                mCollapsedDrawY = mCollapsedBounds.centerY() - textOffset
            }
            else -> {
                val textOffset: Float = (textHeight / 2)
                mCollapsedDrawY = mCollapsedBounds.centerY() - textOffset
            }
        }
        when (collapsedAbsGravity and GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
            Gravity.CENTER_HORIZONTAL -> mCollapsedDrawX = mCollapsedBounds.centerX() - (width / 2)
            Gravity.RIGHT -> mCollapsedDrawX = mCollapsedBounds.right - width
            Gravity.LEFT -> mCollapsedDrawX = mCollapsedBounds.left.toFloat()
            else -> mCollapsedDrawX = mCollapsedBounds.left.toFloat()
        }
        calculateUsingTextSize(mExpandedTextSize)
        // BEGIN MODIFICATION: calculate width using mTextLayout based on first line and store that padding
        width = if (mTextLayout != null) mTextLayout!!.getLineWidth(0) else 0F
        mExpandedFirstLineDrawX = if (mTextLayout != null) mTextLayout!!.getLineLeft(0) else 0F
        // END MODIFICATION
        val expandedAbsGravity: Int = GravityCompat.getAbsoluteGravity(
            mExpandedTextGravity,
            if (mIsRtl) ViewCompat.LAYOUT_DIRECTION_RTL else ViewCompat.LAYOUT_DIRECTION_LTR
        )
        // BEGIN MODIFICATION: calculate height and Y position using mTextLayout
        textHeight = if (mTextLayout != null) mTextLayout!!.height.toFloat() else 0.toFloat()
        when (expandedAbsGravity and Gravity.VERTICAL_GRAVITY_MASK) {
            Gravity.BOTTOM -> mExpandedDrawY = mExpandedBounds.bottom - textHeight
            Gravity.TOP -> mExpandedDrawY = mExpandedBounds.top.toFloat()
            Gravity.CENTER_VERTICAL -> {
                val textOffset: Float = (textHeight / 2)
                mExpandedDrawY = mExpandedBounds.centerY() - textOffset
            }
            else -> {
                val textOffset: Float = (textHeight / 2)
                mExpandedDrawY = mExpandedBounds.centerY() - textOffset
            }
        }
        when (expandedAbsGravity and GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
            Gravity.CENTER_HORIZONTAL -> mExpandedDrawX = mExpandedBounds.centerX() - (width / 2)
            Gravity.RIGHT -> mExpandedDrawX = mExpandedBounds.right - width
            Gravity.LEFT -> mExpandedDrawX = mExpandedBounds.left.toFloat()
            else -> mExpandedDrawX = mExpandedBounds.left.toFloat()
        }
        // The bounds have changed so we need to clear the texture
        clearTexture()
        // Now reset the text size back to the original
        setInterpolatedTextSize(currentTextSize)
    }

    private fun interpolateBounds(fraction: Float) {
        mCurrentBounds.left = lerp(
            mExpandedBounds.left.toFloat(), mCollapsedBounds.left.toFloat(),
            fraction, mPositionInterpolator
        )
        mCurrentBounds.top = lerp(
            mExpandedDrawY, mCollapsedDrawY,
            fraction, mPositionInterpolator
        )
        mCurrentBounds.right = lerp(
            mExpandedBounds.right.toFloat(), mCollapsedBounds.right.toFloat(),
            fraction, mPositionInterpolator
        )
        mCurrentBounds.bottom = lerp(
            mExpandedBounds.bottom.toFloat(), mCollapsedBounds.bottom.toFloat(),
            fraction, mPositionInterpolator
        )
    }

    fun draw(canvas: Canvas) {
        val saveCount: Int = canvas.save()
        if (mTextToDraw != null && mDrawTitle) {
            val x: Float = mCurrentDrawX
            val y: Float = mCurrentDrawY
            val drawTexture: Boolean = mUseTexture && mExpandedTitleTexture != null
            val ascent: Float
            // MODIFICATION: removed now unused "descent" variable declaration
// Update the TextPaint to the current text size
            mTextPaint.textSize = mCurrentTextSize
            // BEGIN MODIFICATION: new drawing code
            if (drawTexture) {
                ascent = 0f
            } else {
                ascent = mTextPaint.ascent() * mScale
            }
            if (DEBUG_DRAW) { // Just a debug tool, which drawn a magenta rect in the text bounds
                canvas.drawRect(
                    mCurrentBounds.left, y, mCurrentBounds.right,
                    y + mTextLayout!!.height * mScale,
                    (DEBUG_DRAW_PAINT)!!
                )
            }
            if (mScale != 1f) {
                canvas.scale(mScale, mScale, x, y)
            }
            // Compute where to draw mTextLayout for this frame
            val currentExpandedX: Float = mCurrentDrawX + mTextLayout!!.getLineLeft(0) - mExpandedFirstLineDrawX * 2
            if (drawTexture) { // If we should use a texture, draw it instead of text
// Expanded text
                mTexturePaint!!.alpha = (mExpandedTextBlend * 255).toInt()
                canvas.drawBitmap((mExpandedTitleTexture)!!, currentExpandedX, y, mTexturePaint)
                // Collapsed text
                mTexturePaint!!.alpha = (mCollapsedTextBlend * 255).toInt()
                canvas.drawBitmap((mCollapsedTitleTexture)!!, x, y, mTexturePaint)
                // Cross-section between both texts (should stay at alpha = 255)
                mTexturePaint!!.alpha = 255
                canvas.drawBitmap((mCrossSectionTitleTexture)!!, x, y, mTexturePaint)
            } else { // positon expanded text appropriately
                canvas.translate(currentExpandedX, y)
                // Expanded text
                mTextPaint.alpha = (mExpandedTextBlend * 255).toInt()
                mTextLayout!!.draw(canvas)
                // position the overlays
                canvas.translate(x - currentExpandedX, 0f)
                // Collapsed text
                mTextPaint.alpha = (mCollapsedTextBlend * 255).toInt()
                canvas.drawText(
                    (mTextToDrawCollapsed)!!, 0, mTextToDrawCollapsed!!.length, 0f,
                    -ascent / mScale, mTextPaint
                )
                // BEGIN MODIFICATION
// Remove ellipsis for Cross-section animation
                var tmp: String = mTextToDrawCollapsed.toString().trim { it <= ' ' }
                if (tmp.endsWith("\u2026")) {
                    tmp = tmp.substring(0, tmp.length - 1)
                }
                // Cross-section between both texts (should stay at alpha = 255)
                mTextPaint.alpha = 255
                canvas.drawText(
                    tmp,
                    0,
                    if (mTextLayout!!.getLineEnd(0) <= tmp.length) mTextLayout!!.getLineEnd(0) else tmp.length,
                    0f,
                    -ascent / mScale,
                    mTextPaint
                )
                // END MODIFICATION
            }
            // END MODIFICATION
        }
        canvas.restoreToCount(saveCount)
    }

    private fun calculateIsRtl(text: CharSequence?): Boolean {
        val defaultIsRtl: Boolean = (ViewCompat.getLayoutDirection(mView)
                === ViewCompat.LAYOUT_DIRECTION_RTL)
        return (if (defaultIsRtl) TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL else TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR).isRtl(
            text,
            0,
            text!!.length
        )
    }

    private fun setInterpolatedTextSize(textSize: Float) {
        calculateUsingTextSize(textSize)
        // Use our texture if the scale isn't 1.0
        mUseTexture = USE_SCALING_TEXTURE && mScale != 1f
        if (mUseTexture) { // Make sure we have an expanded texture if needed
            ensureExpandedTexture()
            // BEGIN MODIFICATION: added collapsed and cross section textures
            ensureCollapsedTexture()
            ensureCrossSectionTexture()
        }
        ViewCompat.postInvalidateOnAnimation(mView)
        // END MODIFICATION
    }

    // BEGIN MODIFICATION: new setCollapsedTextBlend and setExpandedTextBlend methods
    private fun setCollapsedTextBlend(blend: Float) {
        mCollapsedTextBlend = blend
        ViewCompat.postInvalidateOnAnimation(mView)
    }

    private fun setExpandedTextBlend(blend: Float) {
        mExpandedTextBlend = blend
        ViewCompat.postInvalidateOnAnimation(mView)
    }

    // END MODIFICATION
    private fun areTypefacesDifferent(first: Typeface?, second: Typeface?): Boolean {
        return (first != null && !(first == second)) || (first == null && second != null)
    }

    private fun calculateUsingTextSize(textSize: Float) {
        if (mText == null) return
        val collapsedWidth: Float = mCollapsedBounds.width().toFloat()
        val expandedWidth: Float = mExpandedBounds.width().toFloat()
        val availableWidth: Float
        val newTextSize: Float
        var updateDrawText: Boolean = false
        // BEGIN MODIFICATION: Add maxLines variable
        val maxLines: Int
        // END MODIFICATION
        if (isClose(textSize, mCollapsedTextSize)) {
            newTextSize = mCollapsedTextSize
            mScale = 1f
            if (areTypefacesDifferent(mCurrentTypeface, mCollapsedTypeface)) {
                mCurrentTypeface = mCollapsedTypeface
                updateDrawText = true
            }
            availableWidth = collapsedWidth
            // BEGIN MODIFICATION: Set maxLines variable
            maxLines = 1
            // END MODIFICATION
        } else {
            newTextSize = mExpandedTextSize
            if (areTypefacesDifferent(mCurrentTypeface, mExpandedTypeface)) {
                mCurrentTypeface = mExpandedTypeface
                updateDrawText = true
            }
            if (isClose(
                    textSize,
                    mExpandedTextSize
                )
            ) { // If we're close to the expanded text size, snap to it and use a scale of 1
                mScale = 1f
            } else { // Else, we'll scale down from the expanded text size
                mScale = textSize / mExpandedTextSize
            }
            val textSizeRatio: Float = mCollapsedTextSize / mExpandedTextSize
            // This is the size of the expanded bounds when it is scaled to match the
// collapsed text size
            val scaledDownWidth: Float = expandedWidth * textSizeRatio
            if (scaledDownWidth > collapsedWidth) { // If the scaled down size is larger than the actual collapsed width, we need to
// cap the available width so that when the expanded text scales down, it matches
// the collapsed width
// BEGIN MODIFICATION:
                availableWidth = expandedWidth
                // END MODIFICATION
            } else { // Otherwise we'll just use the expanded width
                availableWidth = expandedWidth
            }
            // BEGIN MODIFICATION: Set maxLines variable
            maxLines = this.maxLines
            // END MODIFICATION
        }
        if (availableWidth > 0) {
            updateDrawText = (mCurrentTextSize != newTextSize) || mBoundsChanged || updateDrawText
            mCurrentTextSize = newTextSize
            mBoundsChanged = false
        }
        if (mTextToDraw == null || updateDrawText) {
            mTextPaint.textSize = mCurrentTextSize
            mTextPaint.typeface = mCurrentTypeface
            // BEGIN MODIFICATION: Text layout creation and text truncation
            val layout: StaticLayout = StaticLayout(
                mText, mTextPaint, availableWidth.toInt(),
                Layout.Alignment.ALIGN_NORMAL, lineSpacingMultiplier, lineSpacingExtra, false
            )
            val truncatedText: CharSequence
            if (layout.lineCount > maxLines) {
                val lastLine: Int = maxLines - 1
                val textBefore: CharSequence = if (lastLine > 0) mText!!.subSequence(0, layout.getLineEnd(lastLine - 1)) else ""
                var lineText: CharSequence = mText!!.subSequence(
                    layout.getLineStart(lastLine),
                    layout.getLineEnd(lastLine)
                )
                // if last char in line is space, move it behind the ellipsis
                var lineEnd: CharSequence? = ""
                if (lineText.get(lineText.length - 1) == ' ') {
                    lineEnd = lineText.subSequence(lineText.length - 1, lineText.length)
                    lineText = lineText.subSequence(0, lineText.length - 1)
                }
                // insert ellipsis character
                lineText = TextUtils.concat(lineText, "\u2026", lineEnd)
                // if the text is too long, truncate it
                val truncatedLineText: CharSequence = TextUtils.ellipsize(
                    lineText, mTextPaint,
                    availableWidth, TextUtils.TruncateAt.END
                )
                truncatedText = TextUtils.concat(textBefore, truncatedLineText)
            } else {
                truncatedText = mText ?: ""
            }
            if (!TextUtils.equals(truncatedText, mTextToDraw)) {
                mTextToDraw = truncatedText
                mIsRtl = calculateIsRtl(mTextToDraw)
            }
            val alignment: Layout.Alignment
            when (mExpandedTextGravity and GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
                Gravity.CENTER_HORIZONTAL -> alignment = Layout.Alignment.ALIGN_CENTER
                Gravity.RIGHT, Gravity.END -> alignment = Layout.Alignment.ALIGN_OPPOSITE
                Gravity.LEFT, Gravity.START -> alignment = Layout.Alignment.ALIGN_NORMAL
                else -> alignment = Layout.Alignment.ALIGN_NORMAL
            }
            mTextLayout = StaticLayout(
                mTextToDraw, mTextPaint, availableWidth.toInt(),
                alignment, lineSpacingMultiplier, lineSpacingExtra, false
            )
            // END MODIFICATION
        }
    }

    private fun ensureExpandedTexture() {
        if (((mExpandedTitleTexture != null) || mExpandedBounds.isEmpty
                    || TextUtils.isEmpty(mTextToDraw))
        ) {
            return
        }
        calculateOffsets(0f)
        // BEGIN MODIFICATION: Calculate width and height using mTextLayout and remove
// mTextureAscent and mTextureDescent assignment
        val w: Int = mTextLayout!!.width
        val h: Int = mTextLayout!!.height
        // END MODIFICATION
        if (w <= 0 || h <= 0) {
            return  // If the width or height are 0, return
        }
        mExpandedTitleTexture = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        // BEGIN MODIFICATION: Draw text using mTextLayout
        val c: Canvas = Canvas(this.mExpandedTitleTexture!!)
        mTextLayout!!.draw(c)
        // END MODIFICATION
        if (mTexturePaint == null) { // Make sure we have a paint
            mTexturePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        }
    }

    // BEGIN MODIFICATION: new ensureCollapsedTexture and ensureCrossSectionTexture methods
    private fun ensureCollapsedTexture() {
        if (((mCollapsedTitleTexture != null) || mCollapsedBounds.isEmpty
                    || TextUtils.isEmpty(mTextToDraw))
        ) {
            return
        }
        calculateOffsets(0f)
        val w: Int = Math.round(mTextPaint.measureText(mTextToDraw, 0, mTextToDraw!!.length))
        val h: Int = Math.round(mTextPaint.descent() - mTextPaint.ascent())
        if (w <= 0 && h <= 0) {
            return  // If the width or height are 0, return
        }
        mCollapsedTitleTexture = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c: Canvas = Canvas(mCollapsedTitleTexture!!)
        c.drawText(
            (mTextToDrawCollapsed)!!, 0, mTextToDrawCollapsed!!.length, 0f,
            -mTextPaint.ascent() / mScale, mTextPaint
        )
        if (mTexturePaint == null) { // Make sure we have a paint
            mTexturePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        }
    }

    private fun ensureCrossSectionTexture() {
        if (((mCrossSectionTitleTexture != null) || mCollapsedBounds.isEmpty
                    || TextUtils.isEmpty(mTextToDraw))
        ) {
            return
        }
        calculateOffsets(0f)
        val w: Int = Math.round(
            mTextPaint.measureText(
                mTextToDraw, mTextLayout!!.getLineStart(0),
                mTextLayout!!.getLineEnd(0)
            )
        )
        val h: Int = Math.round(mTextPaint.descent() - mTextPaint.ascent())
        if (w <= 0 && h <= 0) {
            return  // If the width or height are 0, return
        }
        mCrossSectionTitleTexture = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c: Canvas = Canvas(mCrossSectionTitleTexture!!)
        var tmp: String = mTextToDrawCollapsed.toString().trim { it <= ' ' }
        if (tmp.endsWith("\u2026")) {
            tmp = tmp.substring(0, tmp.length - 1)
        }
        c.drawText(
            tmp,
            0,
            if (mTextLayout!!.getLineEnd(0) <= tmp.length) mTextLayout!!.getLineEnd(0) else tmp.length,
            0f,
            -mTextPaint.ascent() / mScale,
            mTextPaint
        )
        if (mTexturePaint == null) { // Make sure we have a paint
            mTexturePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        }
    }

    // END MODIFICATION
    fun recalculate() {
        if (mView.height > 0 && mView.width > 0) { // If we've already been laid out, calculate everything now otherwise we'll wait
// until a layout
            calculateBaseOffsets()
            calculateCurrentOffsets()
        }
    }

    /**
     * Set the title to display
     *
     * @param text
     */
    var text: CharSequence?
        get() {
            return mText
        }
        set(text) {
            if (text == null || !(text == mText)) {
                mText = text
                mTextToDraw = null
                clearTexture()
                recalculate()
            }
        }

    private fun clearTexture() {
        if (mExpandedTitleTexture != null) {
            mExpandedTitleTexture!!.recycle()
            mExpandedTitleTexture = null
        }
        // BEGIN MODIFICATION: clear other textures
        if (mCollapsedTitleTexture != null) {
            mCollapsedTitleTexture!!.recycle()
            mCollapsedTitleTexture = null
        }
        if (mCrossSectionTitleTexture != null) {
            mCrossSectionTitleTexture!!.recycle()
            mCrossSectionTitleTexture = null
        }
        // END MODIFICATION
    }

    // END MODIFICATION
    init {
        mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG)
        mCollapsedBounds = Rect()
        mExpandedBounds = Rect()
        mCurrentBounds = RectF()
    }
}