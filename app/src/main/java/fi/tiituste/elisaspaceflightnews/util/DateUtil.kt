package fi.tiituste.elisaspaceflightnews.util

import android.util.Log
import java.lang.Exception
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class DateUtil {
    companion object {
        fun getFormattedDate(date: String?, format: FormatStyle): String? {
            try {
                val oldFormat =
                    DateTimeFormatter
                        .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                        .withZone(ZoneId.systemDefault())
                val dateObj = LocalDateTime.parse(date, oldFormat)

                return DateTimeFormatter
                    .ofLocalizedDateTime(format)
                    .withZone(ZoneId.systemDefault())
                    .format(dateObj)
            } catch (e: Exception) {
                return null
            }
        }
    }
}