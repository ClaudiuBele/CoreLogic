package dk.sidereal.corelogic.util

import android.content.Context
import dk.sidereal.corelogic.R
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    val SECOND = 1000L
    val MINUTE = SECOND * 60
    val HOUR = MINUTE * 60
    val DAY = HOUR * 24
    val WEEK = DAY * 7
    val MONTH = DAY * 30
    val YEAR = DAY * 365

    fun formatDate(date: Date?): String {
        if (date == null || date.time == -1L) {
            return ""
        }
        return SimpleDateFormat.getDateInstance().format(date)
    }

    fun timeBetween(date1: Long, date2: Long, context: Context): String {
        val timeDiff = date2 - date1
        return when {
            timeDiff < SECOND -> {
                context.getString(R.string.general_moments_ago)
            }
            timeDiff < MINUTE -> {
                val seconds = timeDiff / SECOND
                context.resources.getQuantityString(R.plurals.general__time__seconds, seconds.toInt(), seconds.toInt())
            }
            timeDiff < HOUR -> {
                val minutes = timeDiff / MINUTE
                context.resources.getQuantityString(R.plurals.general__time__minute, minutes.toInt(), minutes.toInt())
            }
            timeDiff < DAY -> {
                val hours = timeDiff / HOUR
                context.resources.getQuantityString(R.plurals.general__time__hour, hours.toInt(), hours.toInt())
            }
            timeDiff < WEEK -> {
                val days = timeDiff / DAY
                context.resources.getQuantityString(R.plurals.general__time__day, days.toInt(), days.toInt())
            }
            timeDiff < MONTH -> {
                val days = timeDiff / WEEK
                context.resources.getQuantityString(R.plurals.general__time__week, days.toInt(), days.toInt())
            }
            timeDiff < YEAR -> {
                val days = timeDiff / MONTH
                context.resources.getQuantityString(R.plurals.general__time__month, days.toInt(), days.toInt())
            }
            else -> {
                val years = timeDiff / YEAR
                context.resources.getQuantityString(R.plurals.general__time__year, years.toInt(), years.toInt())
            }
        }
    }

    fun timeAgo(since: Long, context: Context): String {
        val baseText = timeBetween(since, TimeUtils.now(), context)
        return context.getString(R.string.general__time__ago, baseText)
    }

    fun formatDateToPrefKey(date: Date, prefix: String? = null): String {
        val formattedDate = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(date)
        if (prefix.isNullOrEmpty()) {
            return formattedDate
        } else {
            return "$prefix-$formattedDate"
        }
    }

    fun formatDateToPrefKey(time: Long, prefix: String? = null): String {
        return formatDateToPrefKey(Date(time), prefix)
    }

    fun formatTodayToPrefKey(prefix: String? = null): String {
        return formatDateToPrefKey(TimeUtils.now(), prefix)
    }

}