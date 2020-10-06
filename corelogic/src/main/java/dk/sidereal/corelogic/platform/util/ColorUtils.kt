package dk.sidereal.corelogic.platform.util

import android.graphics.Color
import androidx.annotation.ColorInt

object ColorUtils {

    class ColorData{

        constructor(r:Float,g: Float, b: Float, a: Float ) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a
        }

        var r: Float = 0f
        var g: Float = 0f
        var b: Float = 0f
        var a: Float = 0f
    }

    const val ALPHA_CHANNEL: Byte = 24
    const val RED_CHANNEL: Byte = 16
    const val GREEN_CHANNEL: Byte = 8
    const val BLUE_CHANNEL: Byte = 0

    fun mixTwoColors(color1: Int, color2: Int, amount: Float): Int {

        val inverseAmount = 1.0f - amount
        val a = ((color1 shr ALPHA_CHANNEL.toInt() and 0xff).toFloat() * amount +
                (color2 shr ALPHA_CHANNEL.toInt() and 0xff).toFloat() * inverseAmount).toInt() and 0xff
        val r = ((color1 shr RED_CHANNEL.toInt() and 0xff).toFloat() * amount +
                (color2 shr RED_CHANNEL.toInt() and 0xff).toFloat() * inverseAmount).toInt() and 0xff
        val g = ((color1 shr GREEN_CHANNEL.toInt() and 0xff).toFloat() * amount +
                (color2 shr GREEN_CHANNEL.toInt() and 0xff).toFloat() * inverseAmount).toInt() and 0xff
        val b = ((color1 and 0xff).toFloat() * amount +
                (color2 and 0xff).toFloat() * inverseAmount).toInt() and 0xff
        return a shl ALPHA_CHANNEL.toInt() or (r shl RED_CHANNEL.toInt()) or (g shl GREEN_CHANNEL.toInt()) or (b shl BLUE_CHANNEL.toInt())
    }

    fun valueOf(@ColorInt color: Int): ColorData? {
        val r = (color shr 16 and 0xff) / 255.0f
        val g = (color shr 8 and 0xff) / 255.0f
        val b = (color and 0xff) / 255.0f
        val a = (color shr 24 and 0xff) / 255.0f
        return ColorData(r, g, b, a)
    }

    @ColorInt
    fun adjustAlpha(@ColorInt color: Int, factor: Float): Int {
        val alpha = Math.round(Color.alpha(color) * factor).toInt()
        val red: Int = Color.red(color)
        val green: Int = Color.green(color)
        val blue: Int = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }
}