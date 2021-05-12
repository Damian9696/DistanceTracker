package com.example.distancetracker.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.distancetracker.util.Constants.LOCATION_FASTEST_UPDATE_INTERVAL
import com.example.distancetracker.util.Constants.LOCATION_UPDATE_INTERVAL
import com.example.distancetracker.util.Constants.NOTIFICATION_CHANNEL_ID
import com.example.distancetracker.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.distancetracker.util.Constants.NOTIFICATION_ID
import com.example.distancetracker.util.Constants.SERVICE_START
import com.example.distancetracker.util.Constants.SERVICE_STOP
import com.example.distancetracker.util.toLatLng
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalStateException
import javax.inject.Inject


@AndroidEntryPoint
class TrackerService : LifecycleService() {

    @Inject
    lateinit var notification: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        val started = MutableLiveData<Boolean>()
        val locationList = MutableLiveData<MutableList<LatLng>>()
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.locations?.let { locations ->
                for (location in locations) {
                    updateLocationList(location)
                }
            }
        }
    }

    private fun initValues() {
        started.value = false
        locationList.value = mutableListOf<LatLng>()
    }

    private fun updateLocationList(location: Location) {
        val latLng = location.toLatLng()
        locationList.value?.apply {
            add(latLng)
            locationList.value = this
        }
    }

    override fun onCreate() {
        initValues()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                SERVICE_START -> {
                    started.value = true
                    startForegroundService()
                    startLocationUpdates()
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

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = LOCATION_UPDATE_INTERVAL
            fastestInterval = LOCATION_FASTEST_UPDATE_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
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