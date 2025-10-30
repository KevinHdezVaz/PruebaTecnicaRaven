package com.example.pruebatecnicaraven.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtils {

    private val dateFormats = listOf(
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd"
    )
    fun getTimeAgo(dateString: String?): String {
        if (dateString.isNullOrBlank()) {
            return ""
        }
        var parsedDate: Date? = null

        for (format in dateFormats) {
            try {
                val sdf = SimpleDateFormat(format, Locale.getDefault())
                sdf.timeZone = TimeZone.getTimeZone("UTC")
                parsedDate = sdf.parse(dateString)

                if (parsedDate != null) {
                    break
                }
            } catch (e: Exception) {
                continue
            }
        }
        if (parsedDate == null) {
            return ""
        }
        val now = Date()
        val diffInMillis = now.time - parsedDate.time

        val result = when {
            diffInMillis < 0 -> {
                "ahora"
            }

            diffInMillis < TimeUnit.MINUTES.toMillis(1) -> {
                "ahora"
            }

            diffInMillis < TimeUnit.HOURS.toMillis(1) -> {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
                "${minutes}m"
            }

            diffInMillis < TimeUnit.DAYS.toMillis(1) -> {
                val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
                "${hours}h"
            }

            diffInMillis < TimeUnit.DAYS.toMillis(7) -> {
                val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
                "${days}d"
            }

            diffInMillis < TimeUnit.DAYS.toMillis(30) -> {
                val weeks = TimeUnit.MILLISECONDS.toDays(diffInMillis) / 7
                "${weeks}w"
            }

            diffInMillis < TimeUnit.DAYS.toMillis(365) -> {
                val months = TimeUnit.MILLISECONDS.toDays(diffInMillis) / 30
                "${months}mo"
            }

            else -> {
                val years = TimeUnit.MILLISECONDS.toDays(diffInMillis) / 365
                "${years}y"
            }
        }
        return result
    }

}