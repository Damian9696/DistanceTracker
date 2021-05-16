package com.example.distancetracker.util

object TimeUtil {

    fun calculateElapsedTime(startTime: Long, stopTime: Long): String {
        val elapsedTime = stopTime - startTime

        val seconds = (elapsedTime / 1000).toInt() % 60
        val minutes = (elapsedTime / (1000 * 60) % 60)
        val hours = (elapsedTime / (1000 * 60 * 60) % 24)

        val hourString = if (hours < 10) "0$hours" else "$hours"
        val minutesString = if (minutes < 10) "0$minutes" else "$minutes"
        val secondsString = if (seconds < 10) "0$seconds" else "$seconds"

        return "$hourString:$minutesString:$secondsString"
    }
}