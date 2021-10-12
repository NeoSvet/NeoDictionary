package ru.neosvet.app.utils

import java.util.*
import kotlin.math.floor

class TimeFormatter(private val timeStrings: Array<String>) : ITimeFormatter {
    private val DAY_IN_MILLS = 86400000

    override fun format(time: Long): String {
        val diff = System.currentTimeMillis() - time
        if (diff < DAY_IN_MILLS)
            return diffToString(diff)
        else
            return Date(time).toLocaleString()
    }

    private fun diffToString(diff: Long): String {
        var time = diff / 1000f //to sec
        val k: Int
        if (time < 59.95f) { //sec
            if (time == 0f) time = 1f
            k = 0
        } else {
            time /= 60f
            if (time < 59.95f) //min
                k = 3
            else { //hours
                time /= 60f
                k = 6
            }
        }

        return if (time > 4.95f && time < 20.95f)
            formatFloat(time) + timeStrings[1 + k]
        else if (time == 1f)
            timeStrings[k]
        else {
            var n = if (time - floor(time.toDouble()) < 0.95f) 0 else 1
            n = (time.toInt() + n) % 10
            when (n) {
                1 -> formatFloat(time) + " " + timeStrings[k]
                in 2..4 -> formatFloat(time) + timeStrings[2 + k]
                else -> formatFloat(time) + timeStrings[1 + k]
            }
        }
    }

    private fun formatFloat(f: Float): String {
        val s = java.lang.String.format(Locale.FRANCE, "%.1f", f) //France for dot as ","
        return if (s.contains(",0")) s.substring(0, s.length - 2) else s
    }
}