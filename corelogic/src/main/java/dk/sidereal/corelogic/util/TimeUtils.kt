package dk.sidereal.corelogic.util

import android.content.Context
import dk.sidereal.corelogic.R
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    fun now(): Long = System.currentTimeMillis()


    val SECOND = 1000L
    val MINUTE = SECOND * 60
    val HOUR = MINUTE * 60
    val DAY = HOUR * 24
    val WEEK = DAY * 7
    val MONTH = DAY * 30
    val YEAR = DAY * 365

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

        val minutes =
            if (remainingDuration > MILLIS_MINUTE) (remainingDuration / MILLIS_MINUTE).toInt() else 0
        remainingDuration -= minutes * MILLIS_MINUTE

        val seconds =
            if (remainingDuration > MILLIS_SECOND) (remainingDuration / MILLIS_SECOND).toInt() else 0
        remainingDuration -= seconds * MILLIS_SECOND

        if (hours != 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            return String.format("%02d:%02d", minutes, seconds)
        }
    }

    fun formatDate(date: Date?): String {
        if (date == null || date.time == -1L) {
            return ""
        }
        return SimpleDateFormat.getDateInstance().format(date)
    }

    fun timeBetween(date1: Long, date2: Long, context: Context): String {
        val timeDiff = date2 - date1
        return when {
            timeDiff < MILLIS_SECOND -> {
                context.getString(R.string.general_moments_ago)
            }
            timeDiff < MILLIS_MINUTE -> {
                val seconds = timeDiff / MILLIS_SECOND
                context.resources.getQuantityString(
                    R.plurals.general__time__seconds,
                    seconds.toInt(),
                    seconds.toInt()
                )
            }
            timeDiff < MILLIS_HOUR -> {
                val minutes = timeDiff / MILLIS_MINUTE
                context.resources.getQuantityString(
                    R.plurals.general__time__minute,
                    minutes.toInt(),
                    minutes.toInt()
                )
            }
            timeDiff < MILLIS_DAY -> {
                val hours = timeDiff / MILLIS_HOUR
                context.resources.getQuantityString(
                    R.plurals.general__time__hour,
                    hours.toInt(),
                    hours.toInt()
                )
            }
            timeDiff < MILLIS_WEEK -> {
                val days = timeDiff / MILLIS_DAY
                context.resources.getQuantityString(
                    R.plurals.general__time__day,
                    days.toInt(),
                    days.toInt()
                )
            }
            timeDiff < MILLIS_MONTH -> {
                val days = timeDiff / MILLIS_WEEK
                context.resources.getQuantityString(
                    R.plurals.general__time__week,
                    days.toInt(),
                    days.toInt()
                )
            }
            timeDiff < MILLIS_YEAR -> {
                val days = timeDiff / MILLIS_MONTH
                context.resources.getQuantityString(
                    R.plurals.general__time__month,
                    days.toInt(),
                    days.toInt()
                )
            }
            else -> {
                val years = timeDiff / MILLIS_YEAR
                context.resources.getQuantityString(
                    R.plurals.general__time__year,
                    years.toInt(),
                    years.toInt()
                )
            }
        }
    }

    fun timeAgo(since: Long, context: Context): String {
        val baseText = timeBetween(since, TimeUtils.now(), context)
        return context.getString(R.string.general__time__ago, baseText)
    }

}