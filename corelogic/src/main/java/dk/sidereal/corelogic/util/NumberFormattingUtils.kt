package dk.sidereal.corelogic.util

import android.content.Context
import dk.sidereal.corelogic.R
import dk.sidereal.corelogic.kotlin.ext.simpleTagName

object NumberFormattingUtils {
    /**
     * [Deprecated] use [TimeUtils.getDurationSince]
     */
    fun getDurationSince(context: Context, time: Long): String {
        return TimeUtils.getDurationSince(context, time)
    }

    /** Get time as stopwatch, e.g. 02:50:09 or 20:09
     *
     * [Deprecated], use [TimeUtils.getStopwatchTime]
     */
    fun getStopwatchTime(time: Long): String {
        return TimeUtils.getStopwatchTime(time)
    }

    /** Gets amount done as String. Uses [R.plurals.percentage_done].
     *
     * @param percentDone Must be [0, 100], otherwise we throw [IllegalArgumentException]
     * @param context Application context
     *
     * @throws [IllegalArgumentException] if parameter is not within proper bounds
     */
    fun getPercentDone(context: Context, percentDone: Int): String {
        if (percentDone < 0 || percentDone > 100) {
            throw IllegalArgumentException("${this.javaClass.simpleTagName()}: getPercentDone parameter is not between 0 and 100")
        }
        return context.resources.getQuantityString(
            R.plurals.percentage_done,
            percentDone,
            percentDone
        )
    }


}