package com.example.distancetracker.util

import com.example.distancetracker.util.Constants.SERVICE_START
import com.example.distancetracker.util.Constants.SERVICE_STOP

enum class ServiceEnum(val action: String) {
    ACTION_SERVICE_START(SERVICE_START),
    ACTION_SERVICE_STOP(SERVICE_STOP)
}