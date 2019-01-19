package dk.sidereal.corelogic.util

import android.content.Context
import dk.sidereal.corelogic.R
import dk.sidereal.corelogic.kotlin.ext.simpleTagName

object NumberFormattingUtils {

    const val MILLIS_SECOND = 1000
    const val MILLIS_MINUTE = MILLIS_SECOND * 60
    const val MILLIS_HOUR = MILLIS_MINUTE * 60
    const val MILLIS_DAY = MILLIS_HOUR * 24
    const val MILLIS_WEEK = MILLIS_DAY * 7L
    const val MILLIS_MONTH = MILLIS_DAY * 30L
    const val MILLIS_YEAR: Long = MILLIS_DAY * 365L


    fun getDurationSince(context: Context, time: Long): String {
        val now = TimeUtils.now()
        val duration = now - time
        if (duration > MILLIS_YEAR) {
            val quantity = (duration / MILLIS_YEAR).toInt()
            return context.resources.getQuantityString(R.plurals.time_years, quantity, quantity)
        } else if (duration > MILLIS_MONTH) {
            val quantity = (duration / MILLIS_MONTH).toInt()
            return context.resources.getQuantityString(R.plurals.time_months, quantity, quantity)
        } else if (duration > MILLIS_WEEK) {
            val quantity = (duration / MILLIS_WEEK).toInt()
            return context.resources.getQuantityString(R.plurals.time_weeks, quantity, quantity)
        } else if (duration > MILLIS_DAY) {
            val quantity = (duration / MILLIS_DAY).toInt()
            return context.resources.getQuantityString(R.plurals.time_days, quantity, quantity)
        } else if (duration > MILLIS_HOUR) {
            val quantity = (duration / MILLIS_HOUR).toInt()
            return context.resources.getQuantityString(R.plurals.time_hours, quantity, quantity)
        } else if (duration > MILLIS_MINUTE) {
            val quantity = (duration / MILLIS_MINUTE).toInt()
            return context.resources.getQuantityString(R.plurals.time_minutes, quantity, quantity)
        } else if (duration > MILLIS_SECOND) {
            val quantity = (duration / MILLIS_SECOND).toInt()
            return context.resources.getQuantityString(R.plurals.time_seconds, quantity, quantity)
        } else
            return context.getString(R.string.time_less_than_a_second)
    }

    /** Get time as stopwatch, e.g. 02:50:09 or 20:09
     *
     */
    fun getStopwatchTime(time: Long): String {
        val now = TimeUtils.now()
        val duration = now - time;
        var remainingDuration = duration
        val hours = if (duration > MILLIS_HOUR) (remainingDuration / MILLIS_HOUR).toInt() else 0
        remainingDuration -= hours * MILLIS_HOUR

        val minutes = if (remainingDuration > MILLIS_MINUTE) (remainingDuration / MILLIS_MINUTE).toInt() else 0
        remainingDuration -= minutes * MILLIS_MINUTE

        val seconds = if (remainingDuration > MILLIS_SECOND) (remainingDuration / MILLIS_SECOND).toInt() else 0
        remainingDuration -= seconds * MILLIS_SECOND

        if (hours != 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            return String.format("%02d:%02d", minutes, seconds)
        }
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
        return context.resources.getQuantityString(R.plurals.percentage_done, percentDone, percentDone)
    }


}