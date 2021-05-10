package com.example.distancetracker.service

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.distancetracker.util.Constants.SERVICE_START
import com.example.distancetracker.util.Constants.SERVICE_STOP
import java.lang.IllegalStateException

class TrackerService : LifecycleService() {

    companion object {
        val _started = MutableLiveData<Boolean>()
    }

    private fun initValues() {
        _started.value = false
    }

    override fun onCreate() {
        initValues()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                SERVICE_START -> _started.value = true
                SERVICE_STOP -> _started.value = false
                else -> throw IllegalStateException("Action is not available. Action must be SERVICE_START or SERVICE_STOP.")
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }
}