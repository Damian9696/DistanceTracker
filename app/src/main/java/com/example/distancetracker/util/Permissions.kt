package com.example.distancetracker.util

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment
import com.example.distancetracker.R
import com.example.distancetracker.util.Constants.PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE
import com.example.distancetracker.util.Constants.PERMISSION_LOCATION_REQUEST_CODE
import com.vmadalin.easypermissions.EasyPermissions

object Permissions {

    fun hasLocationPermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    fun hasBackgroundLocationPermission(context: Context) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        } else {
            true
        }


    fun requestLocationPermission(host: Fragment) {
        host.context?.let { context ->
            EasyPermissions.requestPermissions(
                host,
                rationale = context.getString(R.string.permission_rationale_message),
                requestCode = PERMISSION_LOCATION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    fun requestBackgroundLocationPermission(host: Fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            host.context?.let { context ->
                EasyPermissions.requestPermissions(
                    host,
                    rationale = context.getString(R.string.permission_rationale_message),
                    requestCode = PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            }
        }
    }

}