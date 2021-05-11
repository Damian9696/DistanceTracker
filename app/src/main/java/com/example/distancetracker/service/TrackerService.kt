package com.example.distancetracker.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.distancetracker.util.Constants.NOTIFICATION_CHANNEL_ID
import com.example.distancetracker.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.distancetracker.util.Constants.NOTIFICATION_ID
import com.example.distancetracker.util.Constants.SERVICE_START
import com.example.distancetracker.util.Constants.SERVICE_STOP
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalStateException
import javax.inject.Inject

@AndroidEntryPoint
class TrackerService : LifecycleService() {

    @Inject
    lateinit var notification: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    companion object {
        val started = MutableLiveData<Boolean>()
    }

    private fun initValues() {
        started.value = false
    }

    override fun onCreate() {
        initValues()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                SERVICE_START -> {
                    started.value = true
                    startForegroundService()
                }
                SERVICE_STOP -> started.value = false
                else -> throw IllegalStateException("Action is not available. Action must be SERVICE_START or SERVICE_STOP.")
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        buildNotificationChannel()
        startForeground(
            NOTIFICATION_ID,
            notification.build()
        )
    }

    private fun buildNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}